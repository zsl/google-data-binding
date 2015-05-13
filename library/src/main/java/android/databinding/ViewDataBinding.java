/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.databinding;

import com.android.databinding.library.R;

import android.annotation.TargetApi;
import android.databinding.CallbackRegistry.NotifierCallback;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.Choreographer;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

public abstract class ViewDataBinding {

    /**
     * Instead of directly accessing Build.VERSION.SDK_INT, generated code uses this value so that
     * we can test API dependent behavior.
     */
    static int SDK_INT = VERSION.SDK_INT;

    private static final int REBIND = 1;
    private static final int HALTED = 2;
    private static final int REBOUND = 3;

    /**
     * Prefix for android:tag on Views with binding. The root View and include tags will not have
     * android:tag attributes and will use ids instead.
     */
    public static final String BINDING_TAG_PREFIX = "binding_";

    // The length of BINDING_TAG_PREFIX prevents calling length repeatedly.
    private static final int BINDING_NUMBER_START = BINDING_TAG_PREFIX.length();

    // ICS (v 14) fixes a leak when using setTag(int, Object)
    private static final boolean USE_TAG_ID = DataBinderMapper.TARGET_MIN_SDK >= 14;

    private static final boolean USE_CHOREOGRAPHER = SDK_INT >= 16;

    /**
     * Method object extracted out to attach a listener to a bound Observable object.
     */
    private static final CreateWeakListener CREATE_PROPERTY_LISTENER = new CreateWeakListener() {
        @Override
        public WeakListener create(ViewDataBinding viewDataBinding, int localFieldId) {
            return new WeakPropertyListener(viewDataBinding, localFieldId);
        }
    };

    /**
     * Method object extracted out to attach a listener to a bound ObservableList object.
     */
    private static final CreateWeakListener CREATE_LIST_LISTENER = new CreateWeakListener() {
        @Override
        public WeakListener create(ViewDataBinding viewDataBinding, int localFieldId) {
            return new WeakListListener(viewDataBinding, localFieldId);
        }
    };

    /**
     * Method object extracted out to attach a listener to a bound ObservableMap object.
     */
    private static final CreateWeakListener CREATE_MAP_LISTENER = new CreateWeakListener() {
        @Override
        public WeakListener create(ViewDataBinding viewDataBinding, int localFieldId) {
            return new WeakMapListener(viewDataBinding, localFieldId);
        }
    };

    private static final CallbackRegistry.NotifierCallback<OnRebindCallback, ViewDataBinding, Void>
        REBIND_NOTIFIER = new NotifierCallback<OnRebindCallback, ViewDataBinding, Void>() {
        @Override
        public void onNotifyCallback(OnRebindCallback callback, ViewDataBinding sender, int mode,
                Void arg2) {
            switch (mode) {
                case REBIND:
                    if (!callback.onPreBind(sender)) {
                        sender.mRebindHalted = true;
                    }
                    break;
                case HALTED:
                    callback.onCanceled(sender);
                    break;
                case REBOUND:
                    callback.onBound(sender);
                    break;
            }
        }
    };

    private static final OnAttachStateChangeListener ROOT_REATTACHED_LISTENER;

    static {
        if (VERSION.SDK_INT < VERSION_CODES.KITKAT) {
            ROOT_REATTACHED_LISTENER = null;
        } else {
            ROOT_REATTACHED_LISTENER = new OnAttachStateChangeListener() {
                @TargetApi(VERSION_CODES.KITKAT)
                @Override
                public void onViewAttachedToWindow(View v) {
                    // execute the pending bindings.
                    final ViewDataBinding binding;
                    if (USE_TAG_ID) {
                        binding = (ViewDataBinding) v.getTag(R.id.dataBinding);
                    } else {
                        binding = (ViewDataBinding) v.getTag();
                    }
                    binding.mRebindRunnable.run();
                    v.removeOnAttachStateChangeListener(this);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                }
            };
        }
    }

    /**
     * Runnable executed on animation heartbeat to rebind the dirty Views.
     */
    private final Runnable mRebindRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (this) {
                mPendingRebind = false;
            }
            if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
                // Nested so that we don't get a lint warning in IntelliJ
                if (!mRoot.isAttachedToWindow()) {
                    // Don't execute the pending bindings until the View
                    // is attached again.
                    mRoot.removeOnAttachStateChangeListener(ROOT_REATTACHED_LISTENER);
                    mRoot.addOnAttachStateChangeListener(ROOT_REATTACHED_LISTENER);
                    return;
                }
            }
            executePendingBindings();
        }
    };

    /**
     * Flag indicates that there are pending bindings that need to be reevaluated.
     */
    private boolean mPendingRebind = false;

    /**
     * Indicates that a onPreBind has stopped the executePendingBindings call.
     */
    private boolean mRebindHalted = false;

    /**
     * The observed expressions.
     */
    private WeakListener[] mLocalFieldObservers;

    /**
     * The root View that this Binding is associated with.
     */
    private final View mRoot;

    /**
     * The collection of OnRebindCallbacks.
     */
    private CallbackRegistry<OnRebindCallback, ViewDataBinding, Void> mRebindCallbacks;

    /**
     * Flag to prevent reentrant executePendingBinding calls.
     */
    private boolean mIsExecutingPendingBindings;

    // null api < 16
    private Choreographer mChoreographer;

    private final Choreographer.FrameCallback mFrameCallback;

    // null api >= 16
    private Handler mUIThreadHandler;

    protected ViewDataBinding(View root, int localFieldCount) {
        mLocalFieldObservers = new WeakListener[localFieldCount];
        this.mRoot = root;
        if (Looper.myLooper() == null) {
            throw new IllegalStateException("DataBinding must be created in view's UI Thread");
        }
        if (USE_CHOREOGRAPHER) {
            mChoreographer = Choreographer.getInstance();
            mFrameCallback = new Choreographer.FrameCallback() {
                @Override
                public void doFrame(long frameTimeNanos) {
                    mRebindRunnable.run();
                }
            };
        } else {
            mFrameCallback = null;
            mUIThreadHandler = new Handler(Looper.myLooper());
        }
        requestRebind();
    }

    protected void setRootTag(View view) {
        if (USE_TAG_ID) {
            view.setTag(R.id.dataBinding, this);
        } else {
            view.setTag(this);
        }
    }

    protected void setRootTag(View[] views) {
        if (USE_TAG_ID) {
            for (View view : views) {
                view.setTag(R.id.dataBinding, this);
            }
        } else {
            for (View view : views) {
                view.setTag(this);
            }
        }
    }

    public static int getBuildSdkInt() {
        return SDK_INT;
    }

    /**
     * Called when an observed object changes. Sets the appropriate dirty flag if applicable.
     * @param localFieldId The index into mLocalFieldObservers that this Object resides in.
     * @param object The object that has changed.
     * @param fieldId The BR ID of the field being changed or _all if
     *                no specific field is being notified.
     * @return true if this change should cause a change to the UI.
     */
    protected abstract boolean onFieldChange(int localFieldId, Object object, int fieldId);

    /**
     * Set a value value in the Binding class.
     * <p>
     * Typically, the developer will be able to call the subclass's set method directly. For
     * example, if there is a variable <code>x</code> in the Binding, a <code>setX</code> method
     * will be generated. However, there are times when the specific subclass of ViewDataBinding
     * is unknown, so the generated method cannot be discovered without reflection. The
     * setVariable call allows the values of variables to be set without reflection.
     *
     * @param variableId the BR id of the variable to be set. For example, if the variable is
     *                   <code>x</code>, then variableId will be <code>BR.x</code>.
     * @param value The new value of the variable to be set.
     * @return <code>true</code> if the variable exists in the binding or <code>false</code>
     * otherwise.
     */
    public abstract boolean setVariable(int variableId, Object value);

    /**
     * Add a listener to be called when reevaluating dirty fields. This also allows automatic
     * updates to be halted, but does not stop explicit calls to {@link #executePendingBindings()}.
     *
     * @param listener The listener to add.
     */
    public void addOnRebindCallback(OnRebindCallback listener) {
        if (mRebindCallbacks == null) {
            mRebindCallbacks = new CallbackRegistry<OnRebindCallback, ViewDataBinding, Void>(REBIND_NOTIFIER);
        }
        mRebindCallbacks.add(listener);
    }

    /**
     * Removes a listener that was added in {@link #addOnRebindCallback(OnRebindCallback)}.
     *
     * @param listener The listener to remove.
     */
    public void removeOnRebindCallback(OnRebindCallback listener) {
        if (mRebindCallbacks != null) {
            mRebindCallbacks.remove(listener);
        }
    }

    /**
     * Evaluates the pending bindings, updating any Views that have expressions bound to
     * modified variables. This <b>must</b> be run on the UI thread.
     */
    public void executePendingBindings() {
        if (mIsExecutingPendingBindings) {
            requestRebind();
            return;
        }
        if (!hasPendingBindings()) {
            return;
        }
        mIsExecutingPendingBindings = true;
        mRebindHalted = false;
        if (mRebindCallbacks != null) {
            mRebindCallbacks.notifyCallbacks(this, REBIND, null);

            // The onRebindListeners will change mPendingHalted
            if (mRebindHalted) {
                mRebindCallbacks.notifyCallbacks(this, HALTED, null);
            }
        }
        if (!mRebindHalted) {
            executeBindings();
            if (mRebindCallbacks != null) {
                mRebindCallbacks.notifyCallbacks(this, REBOUND, null);
            }
        }
        mIsExecutingPendingBindings = false;
    }

    void forceExecuteBindings() {
        executeBindings();
    }

    protected abstract void executeBindings();

    /**
     * Used internally to invalidate flags of included layouts.
     * @hide
     */
    public abstract void invalidateAll();

    /**
     * @return true if any field has changed and the binding should be evaluated.
     * @hide
     */
    public abstract boolean hasPendingBindings();

    /**
     * Removes binding listeners to expression variables.
     */
    public void unbind() {
        for (WeakListener weakListener : mLocalFieldObservers) {
            if (weakListener != null) {
                weakListener.unregister();
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        unbind();
    }

    static ViewDataBinding getBinding(View v) {
        if (USE_TAG_ID) {
            return (ViewDataBinding) v.getTag(R.id.dataBinding);
        } else {
            final Object tag = v.getTag();
            if (tag instanceof ViewDataBinding) {
                return (ViewDataBinding) tag;
            }
        }
        return null;
    }

    /**
     * Returns the outermost View in the layout file associated with the Binding.
     * @return the outermost View in the layout file associated with the Binding.
     */
    public View getRoot() {
        return mRoot;
    }

    private void handleFieldChange(int mLocalFieldId, Object object, int fieldId) {
        boolean result = onFieldChange(mLocalFieldId, object, fieldId);
        if (result) {
            requestRebind();
        }
    }

    protected boolean unregisterFrom(int localFieldId) {
        WeakListener listener = mLocalFieldObservers[localFieldId];
        if (listener != null) {
            return listener.unregister();
        }
        return false;
    }

    protected void requestRebind() {
        synchronized (this) {
            if (mPendingRebind) {
                return;
            }
            mPendingRebind = true;
        }
        if (USE_CHOREOGRAPHER) {
            mChoreographer.postFrameCallback(mFrameCallback);
        } else {
            mUIThreadHandler.post(mRebindRunnable);
        }

    }

    protected Object getObservedField(int localFieldId) {
        WeakListener listener = mLocalFieldObservers[localFieldId];
        if (listener == null) {
            return null;
        }
        return listener.getTarget();
    }

    private boolean updateRegistration(int localFieldId, Object observable,
            CreateWeakListener listenerCreator) {
        if (observable == null) {
            return unregisterFrom(localFieldId);
        }
        WeakListener listener = mLocalFieldObservers[localFieldId];
        if (listener == null) {
            registerTo(localFieldId, observable, listenerCreator);
            return true;
        }
        if (listener.getTarget() == observable) {
            return false;//nothing to do, same object
        }
        unregisterFrom(localFieldId);
        registerTo(localFieldId, observable, listenerCreator);
        return true;
    }

    protected boolean updateRegistration(int localFieldId, Observable observable) {
        return updateRegistration(localFieldId, observable, CREATE_PROPERTY_LISTENER);
    }

    protected boolean updateRegistration(int localFieldId, ObservableList observable) {
        return updateRegistration(localFieldId, observable, CREATE_LIST_LISTENER);
    }

    protected boolean updateRegistration(int localFieldId, ObservableMap observable) {
        return updateRegistration(localFieldId, observable, CREATE_MAP_LISTENER);
    }

    protected void registerTo(int localFieldId, Object observable,
            CreateWeakListener listenerCreator) {
        if (observable == null) {
            return;
        }
        WeakListener listener = mLocalFieldObservers[localFieldId];
        if (listener == null) {
            listener = listenerCreator.create(this, localFieldId);
            mLocalFieldObservers[localFieldId] = listener;
        }
        listener.setTarget(observable);
    }

    protected static ViewDataBinding bind(View view, int layoutId) {
        return DataBindingUtil.bind(view, layoutId);
    }

    /**
     * Walks the view hierarchy under root and pulls out tagged Views, includes, and views with
     * IDs into an Object[] that is returned. This is used to walk the view hierarchy once to find
     * all bound and ID'd views.
     *
     * @param root The root of the view hierarchy to walk.
     * @param numBindings The total number of ID'd views, views with expressions, and includes
     * @param includes The include layout information, indexed by their container's index.
     * @param viewsWithIds Indexes of views that don't have tags, but have IDs.
     * @return An array of size numBindings containing all Views in the hierarchy that have IDs
     * (with elements in viewsWithIds), are tagged containing expressions, or the bindings for
     * included layouts.
     */
    protected static Object[] mapBindings(View root, int numBindings,
            IncludedLayoutIndex[][] includes, SparseIntArray viewsWithIds) {
        Object[] bindings = new Object[numBindings];
        mapBindings(root, bindings, includes, viewsWithIds, true);
        return bindings;
    }

    /**
     * Walks the view hierarchy under roots and pulls out tagged Views, includes, and views with
     * IDs into an Object[] that is returned. This is used to walk the view hierarchy once to find
     * all bound and ID'd views.
     *
     * @param roots The root Views of the view hierarchy to walk. This is used with merge tags.
     * @param numBindings The total number of ID'd views, views with expressions, and includes
     * @param includes The include layout information, indexed by their container's index.
     * @param viewsWithIds Indexes of views that don't have tags, but have IDs.
     * @return An array of size numBindings containing all Views in the hierarchy that have IDs
     * (with elements in viewsWithIds), are tagged containing expressions, or the bindings for
     * included layouts.
     */
    protected static Object[] mapBindings(View[] roots, int numBindings,
            IncludedLayoutIndex[][] includes, SparseIntArray viewsWithIds) {
        Object[] bindings = new Object[numBindings];
        for (int i = 0; i < roots.length; i++) {
            mapBindings(roots[i], bindings, includes, viewsWithIds, true);
        }
        return bindings;
    }

    private static void mapBindings(View view, Object[] bindings,
            IncludedLayoutIndex[][] includes, SparseIntArray viewsWithIds, boolean isRoot) {
        final IncludedLayoutIndex[] includedLayoutIndexes;
        final String tag = (String) view.getTag();
        boolean isBound = false;
        if (isRoot && tag != null && tag.startsWith("layout")) {
            final int underscoreIndex = tag.lastIndexOf('_');
            if (underscoreIndex > 0 && isNumeric(tag, underscoreIndex + 1)) {
                final int index = parseTagInt(tag, underscoreIndex + 1);
                bindings[index] = view;
                includedLayoutIndexes = includes == null ? null : includes[index];
                isBound = true;
            } else {
                includedLayoutIndexes = null;
            }
        } else if (tag != null && tag.startsWith(BINDING_TAG_PREFIX)) {
            int tagIndex = parseTagInt(tag, BINDING_NUMBER_START);
            bindings[tagIndex] = view;
            isBound = true;
            includedLayoutIndexes = includes == null ? null : includes[tagIndex];
        } else {
            // Not a bound view
            includedLayoutIndexes = null;
        }
        if (!isBound) {
            final int id = view.getId();
            if (id > 0) {
                int index;
                if (viewsWithIds != null && (index = viewsWithIds.get(id, -1)) >= 0) {
                    bindings[index] = view;
                }
            }
        }

        if (view instanceof  ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup) view;
            final int count = viewGroup.getChildCount();
            int minInclude = 0;
            for (int i = 0; i < count; i++) {
                final View child = viewGroup.getChildAt(i);
                boolean isInclude = false;
                if (includedLayoutIndexes != null) {
                    String childTag = (String) child.getTag();
                    if (childTag != null && childTag.endsWith("_0") &&
                            childTag.startsWith("layout") && childTag.indexOf('/') > 0) {
                        // This *could* be an include. Test against the expected includes.
                        int includeIndex = findIncludeIndex(childTag, minInclude,
                                includedLayoutIndexes);
                        if (includeIndex >= 0) {
                            isInclude = true;
                            minInclude = includeIndex + 1;
                            IncludedLayoutIndex include = includedLayoutIndexes[includeIndex];
                            int lastMatchingIndex = findLastMatching(viewGroup, i);
                            if (lastMatchingIndex == i) {
                                bindings[include.index] = DataBindingUtil.bind(child,
                                        include.layoutId);
                            } else {
                                final int includeCount =  lastMatchingIndex - i + 1;
                                final View[] included = new View[includeCount];
                                for (int j = 0; j < includeCount; j++) {
                                    included[j] = viewGroup.getChildAt(i + j);
                                }
                                bindings[include.index] = DataBindingUtil.bind(included,
                                        include.layoutId);
                                i += includeCount - 1;
                            }
                        }
                    }
                }
                if (!isInclude) {
                    mapBindings(child, bindings, includes, viewsWithIds, false);
                }
            }
        }
    }

    private static int findIncludeIndex(String tag, int minInclude,
            IncludedLayoutIndex[] layoutIndexes) {
        final int slashIndex = tag.indexOf('/');
        final CharSequence layoutName = tag.subSequence(slashIndex + 1, tag.length() - 2);

        final int length = layoutIndexes.length;
        for (int i = minInclude; i < length; i++) {
            final IncludedLayoutIndex layoutIndex = layoutIndexes[i];
            if (TextUtils.equals(layoutName, layoutIndex.layout)) {
                return i;
            }
        }
        return -1;
    }

    private static int findLastMatching(ViewGroup viewGroup, int firstIncludedIndex) {
        final View firstView = viewGroup.getChildAt(firstIncludedIndex);
        final String firstViewTag = (String) firstView.getTag();
        final String tagBase = firstViewTag.substring(0, firstViewTag.length() - 1); // don't include the "0"
        final int tagSequenceIndex = tagBase.length();

        final int count = viewGroup.getChildCount();
        int max = firstIncludedIndex;
        for (int i = firstIncludedIndex + 1; i < count; i++) {
            final View view = viewGroup.getChildAt(i);
            final String tag = (String) view.getTag();
            if (tag != null && tag.startsWith(tagBase)) {
                if (tag.length() == firstViewTag.length() && tag.charAt(tag.length() - 1) == '0') {
                    return max; // Found another instance of the include
                }
                if (isNumeric(tag, tagSequenceIndex)) {
                    max = i;
                }
            }
        }
        return max;
    }

    private static boolean isNumeric(String tag, int startIndex) {
        int length = tag.length();
        if (length == startIndex) {
            return false; // no numerals
        }
        for (int i = startIndex; i < length; i++) {
            if (!Character.isDigit(tag.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parse the tag without creating a new String object. This is fast and assumes the
     * tag is in the correct format.
     * @param str The tag string.
     * @return The binding tag number parsed from the tag string.
     */
    private static int parseTagInt(String str, int startIndex) {
        final int end = str.length();
        int val = 0;
        for (int i = startIndex; i < end; i++) {
            val *= 10;
            char c = str.charAt(i);
            val += (c - '0');
        }
        return val;
    }

    private static abstract class WeakListener<T> {
        private final WeakReference<ViewDataBinding> mBinder;
        protected final int mLocalFieldId;
        private T mTarget;

        public WeakListener(ViewDataBinding binder, int localFieldId) {
            mBinder = new WeakReference<ViewDataBinding>(binder);
            mLocalFieldId = localFieldId;
        }

        public void setTarget(T object) {
            unregister();
            mTarget = object;
            if (mTarget != null) {
                addListener(mTarget);
            }
        }

        public boolean unregister() {
            boolean unregistered = false;
            if (mTarget != null) {
                removeListener(mTarget);
                unregistered = true;
            }
            mTarget = null;
            return unregistered;
        }

        public T getTarget() {
            return mTarget;
        }

        protected ViewDataBinding getBinder() {
            ViewDataBinding binder = mBinder.get();
            if (binder == null) {
                unregister(); // The binder is dead
            }
            return binder;
        }

        protected abstract void addListener(T target);
        protected abstract void removeListener(T target);
    }

    private static class WeakPropertyListener extends WeakListener<Observable>
            implements OnPropertyChangedListener {
        public WeakPropertyListener(ViewDataBinding binder, int localFieldId) {
            super(binder, localFieldId);
        }

        @Override
        protected void addListener(Observable target) {
            target.addOnPropertyChangedListener(this);
        }

        @Override
        protected void removeListener(Observable target) {
            target.removeOnPropertyChangedListener(this);
        }

        @Override
        public void onPropertyChanged(Observable sender, int fieldId) {
            ViewDataBinding binder = getBinder();
            if (binder == null) {
                return;
            }
            Observable obj = getTarget();
            if (obj != sender) {
                return; // notification from the wrong object?
            }
            binder.handleFieldChange(mLocalFieldId, sender, fieldId);
        }
    }

    private static class WeakListListener extends WeakListener<ObservableList>
            implements OnListChangedListener {

        public WeakListListener(ViewDataBinding binder, int localFieldId) {
            super(binder, localFieldId);
        }

        @Override
        public void onChanged() {
            ViewDataBinding binder = getBinder();
            if (binder == null) {
                return;
            }
            ObservableList target = getTarget();
            if (target == null) {
                return; // We don't expect any notifications from null targets
            }
            binder.handleFieldChange(mLocalFieldId, target, 0);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        protected void addListener(ObservableList target) {
            target.addOnListChangedListener(this);
        }

        @Override
        protected void removeListener(ObservableList target) {
            target.removeOnListChangedListener(this);
        }
    }

    private static class WeakMapListener extends WeakListener<ObservableMap>
            implements OnMapChangedListener {
        public WeakMapListener(ViewDataBinding binder, int localFieldId) {
            super(binder, localFieldId);
        }

        @Override
        protected void addListener(ObservableMap target) {
            target.addOnMapChangedListener(this);
        }

        @Override
        protected void removeListener(ObservableMap target) {
            target.removeOnMapChangedListener(this);
        }

        @Override
        public void onMapChanged(ObservableMap sender, Object key) {
            ViewDataBinding binder = getBinder();
            if (binder == null || sender != getTarget()) {
                return;
            }
            binder.handleFieldChange(mLocalFieldId, sender, 0);
        }
    }

    private interface CreateWeakListener {
        WeakListener create(ViewDataBinding viewDataBinding, int localFieldId);
    }

    protected static class IncludedLayoutIndex {
        public final String layout;
        public final int index;
        public final int layoutId;

        public IncludedLayoutIndex(String layout, int index, int layoutId) {
            this.layout = layout;
            this.index = index;
            this.layoutId = layoutId;
        }
    }
}
