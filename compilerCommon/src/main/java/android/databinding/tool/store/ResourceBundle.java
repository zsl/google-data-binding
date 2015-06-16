/*
 * Copyright (C) 2015 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.databinding.tool.store;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import org.apache.commons.lang3.ArrayUtils;

import android.databinding.tool.util.L;
import android.databinding.tool.util.ParserHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * This is a serializable class that can keep the result of parsing layout files.
 */
public class ResourceBundle implements Serializable {
    private static final String[] ANDROID_VIEW_PACKAGE_VIEWS = new String[]
            {"View", "ViewGroup", "ViewStub", "TextureView", "SurfaceView"};
    private String mAppPackage;

    private HashMap<String, List<LayoutFileBundle>> mLayoutBundles
            = new HashMap<String, List<LayoutFileBundle>>();

    public ResourceBundle(String appPackage) {
        mAppPackage = appPackage;
    }

    public void addLayoutBundle(LayoutFileBundle bundle) {
        Preconditions.checkArgument(bundle.mFileName != null, "File bundle must have a name");
        if (!mLayoutBundles.containsKey(bundle.mFileName)) {
            mLayoutBundles.put(bundle.mFileName, new ArrayList<LayoutFileBundle>());
        }
        final List<LayoutFileBundle> bundles = mLayoutBundles.get(bundle.mFileName);
        for (LayoutFileBundle existing : bundles) {
            if (existing.equals(bundle)) {
                L.d("skipping layout bundle %s because it already exists.", bundle);
                return;
            }
        }
        L.d("adding bundle %s", bundle);
        bundles.add(bundle);
    }

    public HashMap<String, List<LayoutFileBundle>> getLayoutBundles() {
        return mLayoutBundles;
    }

    public String getAppPackage() {
        return mAppPackage;
    }

    public void validateMultiResLayouts() {
        for (List<LayoutFileBundle> layoutFileBundles : mLayoutBundles.values()) {
            for (LayoutFileBundle layoutFileBundle : layoutFileBundles) {
                for (BindingTargetBundle target : layoutFileBundle.getBindingTargetBundles()) {
                    if (target.isBinder()) {
                        List<LayoutFileBundle> boundTo =
                                mLayoutBundles.get(target.getIncludedLayout());
                        if (boundTo == null || boundTo.isEmpty()) {
                            L.e("There is no binding for %s", target.getIncludedLayout());
                        } else {
                            String binding = boundTo.get(0).getFullBindingClass();
                            target.setInterfaceType(binding);
                        }
                    }
                }
            }
        }

        final Iterable<Map.Entry<String, List<LayoutFileBundle>>> multiResLayouts = Iterables
                .filter(mLayoutBundles.entrySet(),
                        new Predicate<Map.Entry<String, List<LayoutFileBundle>>>() {
                            @Override
                            public boolean apply(Map.Entry<String, List<LayoutFileBundle>> input) {
                                return input.getValue().size() > 1;
                            }
                        });

        for (Map.Entry<String, List<LayoutFileBundle>> bundles : multiResLayouts) {
            // validate all ids are in correct view types
            // and all variables have the same name
            Map<String, String> variableTypes = new HashMap<String, String>();
            Map<String, String> importTypes = new HashMap<String, String>();
            String bindingClass = null;

            for (LayoutFileBundle bundle : bundles.getValue()) {
                bundle.mHasVariations = true;
                if (bindingClass == null) {
                    bindingClass = bundle.getFullBindingClass();
                } else {
                    if (!bindingClass.equals(bundle.getFullBindingClass())) {
                        L.e("Binding class names must match. Layout file for %s have " +
                                        "different binding class names %s and %s",
                                bundle.getFileName(),
                                bindingClass, bundle.getFullBindingClass());
                    }
                }
                for (Map.Entry<String, String> variable : bundle.mVariables.entrySet()) {
                    String existing = variableTypes.get(variable.getKey());
                    Preconditions
                            .checkState(existing == null || existing.equals(variable.getValue()),
                                    "inconsistent variable types for %s for layout %s",
                                    variable.getKey(), bundle.mFileName);
                    variableTypes.put(variable.getKey(), variable.getValue());
                }
                for (Map.Entry<String, String> userImport : bundle.mImports.entrySet()) {
                    String existing = importTypes.get(userImport.getKey());
                    Preconditions
                            .checkState(existing == null || existing.equals(userImport.getValue()),
                                    "inconsistent variable types for %s for layout %s",
                                    userImport.getKey(), bundle.mFileName);
                    importTypes.put(userImport.getKey(), userImport.getValue());
                }
            }

            for (LayoutFileBundle bundle : bundles.getValue()) {
                // now add missing ones to each to ensure they can be referenced
                L.d("checking for missing variables in %s / %s", bundle.mFileName,
                        bundle.mConfigName);
                for (Map.Entry<String, String> variable : variableTypes.entrySet()) {
                    if (!bundle.mVariables.containsKey(variable.getKey())) {
                        bundle.mVariables.put(variable.getKey(), variable.getValue());
                        L.d("adding missing variable %s to %s / %s", variable.getKey(),
                                bundle.mFileName, bundle.mConfigName);
                    }
                }
                for (Map.Entry<String, String> userImport : importTypes.entrySet()) {
                    if (!bundle.mImports.containsKey(userImport.getKey())) {
                        bundle.mImports.put(userImport.getKey(), userImport.getValue());
                        L.d("adding missing import %s to %s / %s", userImport.getKey(),
                                bundle.mFileName, bundle.mConfigName);
                    }
                }
            }

            Set<String> includeBindingIds = new HashSet<String>();
            Set<String> viewBindingIds = new HashSet<String>();
            Map<String, String> viewTypes = new HashMap<String, String>();
            Map<String, String> includes = new HashMap<String, String>();
            L.d("validating ids for %s", bundles.getKey());
            for (LayoutFileBundle bundle : bundles.getValue()) {
                for (BindingTargetBundle target : bundle.mBindingTargetBundles) {
                    L.d("checking %s %s %s", target.getId(), target.getFullClassName(),
                            target.isBinder());
                    if (target.mId != null) {
                        if (target.isBinder()) {
                            Preconditions.checkState(!viewBindingIds.contains(target.getFullClassName()),
                                    "Cannot use the same id for a View and an include tag. Error " +
                                            "in file %s / %s", bundle.mFileName, bundle.mConfigName);
                            includeBindingIds.add(target.getFullClassName());
                        } else {
                            Preconditions.checkState(!includeBindingIds.contains(target.getFullClassName()),
                                    "Cannot use the same id for a View and an include tag. Error in "
                                            + "file %s / %s", bundle.mFileName, bundle.mConfigName);
                            viewBindingIds.add(target.getFullClassName());
                        }
                        String existingType = viewTypes.get(target.mId);
                        if (existingType == null) {
                            L.d("assigning %s as %s", target.getId(), target.getFullClassName());
                            viewTypes.put(target.mId, target.getFullClassName());
                            if (target.isBinder()) {
                                includes.put(target.mId, target.getIncludedLayout());
                            }
                        } else if (!existingType.equals(target.getFullClassName())) {
                            if (target.isBinder()) {
                                L.d("overriding %s as base binder", target.getId());
                                viewTypes.put(target.mId,
                                        "android.databinding.ViewDataBinding");
                                includes.put(target.mId, target.getIncludedLayout());
                            } else {
                                L.d("overriding %s as base view", target.getId());
                                viewTypes.put(target.mId, "android.view.View");
                            }
                        }
                    }
                }
            }

            for (LayoutFileBundle bundle : bundles.getValue()) {
                for (Map.Entry<String, String> viewType : viewTypes.entrySet()) {
                    BindingTargetBundle target = bundle.getBindingTargetById(viewType.getKey());
                    if (target == null) {
                        String include = includes.get(viewType.getKey());
                        if (include == null) {
                            bundle.createBindingTarget(viewType.getKey(), viewType.getValue(),
                                    false, null, null);
                        } else {
                            BindingTargetBundle bindingTargetBundle = bundle.createBindingTarget(
                                    viewType.getKey(), null, false, null, null);
                            bindingTargetBundle.setIncludedLayout(includes.get(viewType.getKey()));
                            bindingTargetBundle.setInterfaceType(viewType.getValue());
                        }
                    } else {
                        L.d("setting interface type on %s (%s) as %s", target.mId, target.getFullClassName(), viewType.getValue());
                        target.setInterfaceType(viewType.getValue());
                    }
                }
            }
        }
        // assign class names to each
        for (Map.Entry<String, List<LayoutFileBundle>> entry : mLayoutBundles.entrySet()) {
            for (LayoutFileBundle bundle : entry.getValue()) {
                final String configName;
                if (bundle.hasVariations()) {
                    // append configuration specifiers.
                    final String parentFileName = bundle.mDirectory;
                    L.d("parent file for %s is %s", bundle.getFileName(), parentFileName);
                    if ("layout".equals(parentFileName)) {
                        configName = "";
                    } else {
                        configName = ParserHelper.toClassName(parentFileName.substring("layout-".length()));
                    }
                } else {
                    configName = "";
                }
                bundle.mConfigName = configName;
            }
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlRootElement(name="Layout")
    public static class LayoutFileBundle implements Serializable {
        @XmlAttribute(name="layout", required = true)
        public String mFileName;
        @XmlAttribute(name="modulePackage", required = true)
        public String mModulePackage;
        private String mConfigName;

        // The binding class as given by the user
        @XmlAttribute(name="bindingClass", required = false)
        public String mBindingClass;

        // The full package and class name as determined from mBindingClass and mModulePackage
        private String mFullBindingClass;

        // The simple binding class name as determined from mBindingClass and mModulePackage
        private String mBindingClassName;

        // The package of the binding class as determined from mBindingClass and mModulePackage
        private String mBindingPackage;

        @XmlAttribute(name="directory", required = true)
        public String mDirectory;
        public boolean mHasVariations;

        @XmlElement(name="Variables")
        @XmlJavaTypeAdapter(NameTypeAdapter.class)
        public Map<String, String> mVariables = new HashMap<String, String>();

        @XmlElement(name="Imports")
        @XmlJavaTypeAdapter(NameTypeAdapter.class)
        public Map<String, String> mImports = new HashMap<String, String>();

        @XmlElementWrapper(name="Targets")
        @XmlElement(name="Target")
        public List<BindingTargetBundle> mBindingTargetBundles = new ArrayList<BindingTargetBundle>();

        @XmlAttribute(name="isMerge", required = true)
        private boolean mIsMerge;

        // for XML binding
        public LayoutFileBundle() {
        }

        public LayoutFileBundle(String fileName, String directory, String modulePackage,
                boolean isMerge) {
            mFileName = fileName;
            mDirectory = directory;
            mModulePackage = modulePackage;
            mIsMerge = isMerge;
        }

        public void addVariable(String name, String type) {
            mVariables.put(name, type);
        }

        public void addImport(String alias, String type) {
            mImports.put(alias, type);
        }

        public BindingTargetBundle createBindingTarget(String id, String viewName,
                boolean used, String tag, String originalTag) {
            BindingTargetBundle target = new BindingTargetBundle(id, viewName, used, tag,
                    originalTag);
            mBindingTargetBundles.add(target);
            return target;
        }

        public boolean isEmpty() {
            return mVariables.isEmpty() && mImports.isEmpty() && mBindingTargetBundles.isEmpty();
        }

        public BindingTargetBundle getBindingTargetById(String key) {
            for (BindingTargetBundle target : mBindingTargetBundles) {
                if (key.equals(target.mId)) {
                    return target;
                }
            }
            return null;
        }

        public String getFileName() {
            return mFileName;
        }

        public String getConfigName() {
            return mConfigName;
        }

        public String getDirectory() {
            return mDirectory;
        }

        public boolean hasVariations() {
            return mHasVariations;
        }

        public Map<String, String> getVariables() {
            return mVariables;
        }

        public Map<String, String> getImports() {
            return mImports;
        }

        public boolean isMerge() {
            return mIsMerge;
        }

        public String getBindingClassName() {
            if (mBindingClassName == null) {
                String fullClass = getFullBindingClass();
                int dotIndex = fullClass.lastIndexOf('.');
                mBindingClassName = fullClass.substring(dotIndex + 1);
            }
            return mBindingClassName;
        }

        public void setBindingClass(String bindingClass) {
            mBindingClass = bindingClass;
        }

        public String getBindingClassPackage() {
            if (mBindingPackage == null) {
                String fullClass = getFullBindingClass();
                int dotIndex = fullClass.lastIndexOf('.');
                mBindingPackage = fullClass.substring(0, dotIndex);
            }
            return mBindingPackage;
        }

        private String getFullBindingClass() {
            if (mFullBindingClass == null) {
                if (mBindingClass == null) {
                    mFullBindingClass = getModulePackage() + ".databinding." +
                            ParserHelper.toClassName(getFileName()) + "Binding";
                } else if (mBindingClass.startsWith(".")) {
                    mFullBindingClass = getModulePackage() + mBindingClass;
                } else if (mBindingClass.indexOf('.') < 0) {
                    mFullBindingClass = getModulePackage() + ".databinding." + mBindingClass;
                } else {
                    mFullBindingClass = mBindingClass;
                }
            }
            return mFullBindingClass;
        }

        public List<BindingTargetBundle> getBindingTargetBundles() {
            return mBindingTargetBundles;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            LayoutFileBundle bundle = (LayoutFileBundle) o;

            if (mConfigName != null ? !mConfigName.equals(bundle.mConfigName)
                    : bundle.mConfigName != null) {
                return false;
            }
            if (mDirectory != null ? !mDirectory.equals(bundle.mDirectory)
                    : bundle.mDirectory != null) {
                return false;
            }
            if (mFileName != null ? !mFileName.equals(bundle.mFileName)
                    : bundle.mFileName != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = mFileName != null ? mFileName.hashCode() : 0;
            result = 31 * result + (mConfigName != null ? mConfigName.hashCode() : 0);
            result = 31 * result + (mDirectory != null ? mDirectory.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "LayoutFileBundle{" +
                    "mHasVariations=" + mHasVariations +
                    ", mDirectory='" + mDirectory + '\'' +
                    ", mConfigName='" + mConfigName + '\'' +
                    ", mModulePackage='" + mModulePackage + '\'' +
                    ", mFileName='" + mFileName + '\'' +
                    '}';
        }

        public String getModulePackage() {
            return mModulePackage;
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    public static class MarshalledNameType {
        @XmlAttribute(name="type", required = true)
        public String type;

        @XmlAttribute(name="name", required = true)
        public String name;
    }

    public static class MarshalledMapType {
        public List<MarshalledNameType> entries;
    }

    @XmlAccessorType(XmlAccessType.NONE)
    public static class BindingTargetBundle implements Serializable {
        // public for XML serialization

        @XmlAttribute(name="id")
        public String mId;
        @XmlAttribute(name="tag", required = true)
        public String mTag;
        @XmlAttribute(name="originalTag")
        public String mOriginalTag;
        @XmlAttribute(name="view", required = false)
        public String mViewName;
        private String mFullClassName;
        public boolean mUsed = true;
        @XmlElementWrapper(name="Expressions")
        @XmlElement(name="Expression")
        public List<BindingBundle> mBindingBundleList = new ArrayList<BindingBundle>();
        @XmlAttribute(name="include")
        public String mIncludedLayout;
        private String mInterfaceType;

        // For XML serialization
        public BindingTargetBundle() {}

        public BindingTargetBundle(String id, String viewName, boolean used,
                String tag, String originalTag) {
            mId = id;
            mViewName = viewName;
            mUsed = used;
            mTag = tag;
            mOriginalTag = originalTag;
        }

        public void addBinding(String name, String expr) {
            mBindingBundleList.add(new BindingBundle(name, expr));
        }

        public void setIncludedLayout(String includedLayout) {
            mIncludedLayout = includedLayout;
        }

        public String getIncludedLayout() {
            return mIncludedLayout;
        }

        public boolean isBinder() {
            return mIncludedLayout != null;
        }

        public void setInterfaceType(String interfaceType) {
            mInterfaceType = interfaceType;
        }

        public String getId() {
            return mId;
        }

        public String getTag() {
            return mTag;
        }

        public String getOriginalTag() {
            return mOriginalTag;
        }

        public String getFullClassName() {
            if (mFullClassName == null) {
                if (isBinder()) {
                    mFullClassName = mInterfaceType;
                } else if (mViewName.indexOf('.') == -1) {
                    if (ArrayUtils.contains(ANDROID_VIEW_PACKAGE_VIEWS, mViewName)) {
                        mFullClassName = "android.view." + mViewName;
                    } else if("WebView".equals(mViewName)) {
                        mFullClassName = "android.webkit." + mViewName;
                    } else {
                        mFullClassName = "android.widget." + mViewName;
                    }
                } else {
                    mFullClassName = mViewName;
                }
            }
            if (mFullClassName == null) {
                L.e("Unexpected full class name = null. view = %s, interface = %s, layout = %s",
                        mViewName, mInterfaceType, mIncludedLayout);
            }
            return mFullClassName;
        }

        public boolean isUsed() {
            return mUsed;
        }

        public List<BindingBundle> getBindingBundleList() {
            return mBindingBundleList;
        }

        public String getInterfaceType() {
            return mInterfaceType;
        }

        @XmlAccessorType(XmlAccessType.NONE)
        public static class BindingBundle implements Serializable {

            private String mName;
            private String mExpr;

            public BindingBundle() {}

            public BindingBundle(String name, String expr) {
                mName = name;
                mExpr = expr;
            }

            @XmlAttribute(name="attribute", required=true)
            public String getName() {
                return mName;
            }

            @XmlAttribute(name="text", required=true)
            public String getExpr() {
                return mExpr;
            }

            public void setName(String name) {
                mName = name;
            }

            public void setExpr(String expr) {
                mExpr = expr;
            }
        }
    }

    private final static class NameTypeAdapter
            extends XmlAdapter<MarshalledMapType, Map<String, String>> {

        @Override
        public HashMap<String, String> unmarshal(MarshalledMapType v) throws Exception {
            HashMap<String, String> map = new HashMap<String, String>();
            if (v.entries != null) {
                for (MarshalledNameType entry : v.entries) {
                    map.put(entry.name, entry.type);
                }
            }
            return map;
        }

        @Override
        public MarshalledMapType marshal(Map<String, String> v) throws Exception {
            if (v.isEmpty()) {
                return null;
            }
            MarshalledMapType marshalled = new MarshalledMapType();
            marshalled.entries = new ArrayList<MarshalledNameType>();
            for (String name : v.keySet()) {
                MarshalledNameType nameType = new MarshalledNameType();
                nameType.name = name;
                nameType.type = v.get(name);
                marshalled.entries.add(nameType);
            }
            return marshalled;
        }
    }
}