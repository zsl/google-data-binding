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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.databinding.tool.util.L;
import android.databinding.tool.util.ParserHelper;
import android.databinding.tool.util.XmlEditor;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Gets the list of XML files and creates a list of
 * {@link android.databinding.tool.store.ResourceBundle} that can be persistent or converted to
 * LayoutBinder.
 */
public class LayoutFileParser {
    private static final String XPATH_VARIABLE_DEFINITIONS = "/layout/data/variable";
    private static final String XPATH_BINDING_ELEMENTS = "/layout/*[name() != 'data' and name() != 'merge'] | /layout/merge/* | //*[include/.. or @*[starts-with(., '@{') and substring(., string-length(.)) = '}']]";
    private static final String XPATH_ID_ELEMENTS = "//*[@*[local-name()='id']]";
    private static final String XPATH_IMPORT_DEFINITIONS = "/layout/data/import";
    private static final String XPATH_MERGE_ELEMENT = "/layout/merge";
    private static final String XPATH_BINDING_LAYOUT = "/layout";
    private static final String XPATH_BINDING_CLASS = "/layout/data/@class";
    final String LAYOUT_PREFIX = "@layout/";

    public ResourceBundle.LayoutFileBundle parseXml(File xml, String pkg, int minSdk)
            throws ParserConfigurationException, IOException, SAXException,
            XPathExpressionException {
        final String xmlNoExtension = ParserHelper.INSTANCE$.stripExtension(xml.getName());
        final String newTag = xml.getParentFile().getName() + '/' + xmlNoExtension;
        File original = stripFileAndGetOriginal(xml, newTag);
        if (original == null) {
            L.d("assuming the file is the original for %s", xml.getAbsoluteFile());
            original = xml;
        }
        L.d("parsing file %s", xml.getAbsolutePath());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document doc = builder.parse(original);

        final XPathFactory xPathFactory = XPathFactory.newInstance();
        final XPath xPath = xPathFactory.newXPath();

        if (!isBindingLayout(doc, xPath)) {
            return null;
        }

        List<Node> variableNodes = getVariableNodes(doc, xPath);
        final List<Node> imports = getImportNodes(doc, xPath);

        ResourceBundle.LayoutFileBundle bundle = new ResourceBundle.LayoutFileBundle(
                xmlNoExtension, xml.getParentFile().getName(), pkg, isMerge(doc, xPath));

        L.d("number of variable nodes %d", variableNodes.size());
        for (Node item : variableNodes) {
            L.d("reading variable node %s", item);
            NamedNodeMap attributes = item.getAttributes();
            String variableName = attributes.getNamedItem("name").getNodeValue();
            String variableType = attributes.getNamedItem("type").getNodeValue();
            L.d("name: %s, type:%s", variableName, variableType);
            bundle.addVariable(variableName, variableType);
        }

        L.d("import node count %d", imports.size());
        for (Node item : imports) {
            NamedNodeMap attributes = item.getAttributes();
            String type = attributes.getNamedItem("type").getNodeValue();
            final Node aliasNode = attributes.getNamedItem("alias");
            final String alias;
            if (aliasNode == null) {
                final String[] split = StringUtils.split(type, '.');
                alias = split[split.length - 1];
            } else {
                alias = aliasNode.getNodeValue();
            }
            bundle.addImport(alias, type);
        }

        final Node layoutParent = getLayoutParent(doc, xPath);
        final List<Node> bindingNodes = getBindingNodes(doc, xPath);
        final HashMap<Node, String> nodeTagMap = new HashMap<Node, String>();
        L.d("number of binding nodes %d", bindingNodes.size());
        int tagNumber = 0;
        for (Node parent : bindingNodes) {
            NamedNodeMap attributes = parent.getAttributes();
            String nodeName = parent.getNodeName();
            String viewName = null;
            String includedLayoutName = null;
            final Node id = attributes.getNamedItem("android:id");
            final String tag;
            final Node originalTag = attributes.getNamedItem("android:tag");
            if ("include".equals(nodeName)) {
                // get the layout attribute
                final Node includedLayout = attributes.getNamedItem("layout");
                Preconditions.checkNotNull(includedLayout, "must include a layout");
                final String includeValue = includedLayout.getNodeValue();
                Preconditions.checkArgument(includeValue.startsWith(LAYOUT_PREFIX));
                // if user is binding something there, there MUST be a layout file to be
                // generated.
                String layoutName = includeValue.substring(LAYOUT_PREFIX.length());
                includedLayoutName = layoutName;
                tag = nodeTagMap.get(parent.getParentNode());
            } else if ("fragment".equals(nodeName)) {
                L.e("fragments do not support data binding expressions: %s", xml.getPath());
                continue;
            } else {
                viewName = getViewName(parent);
                if (layoutParent == parent.getParentNode()) {
                    tag = newTag + "_" + tagNumber;
                } else {
                    tag = "binding_" + tagNumber;
                }
                tagNumber++;
            }
            final ResourceBundle.BindingTargetBundle bindingTargetBundle =
                    bundle.createBindingTarget(id == null ? null : id.getNodeValue(),
                            viewName, true, tag, originalTag == null ? null : originalTag.getNodeValue());
            nodeTagMap.put(parent, tag);
            bindingTargetBundle.setIncludedLayout(includedLayoutName);

            final int attrCount = attributes.getLength();
            for (int i = 0; i < attrCount; i ++) {
                final Node attr = attributes.item(i);
                String value = attr.getNodeValue();
                if (value.charAt(0) == '@' && value.charAt(1) == '{' &&
                        value.charAt(value.length() - 1) == '}') {
                    final String strippedValue = value.substring(2, value.length() - 1);
                    bindingTargetBundle.addBinding(attr.getNodeName(), strippedValue);
                }
            }
        }

        final List<Node> idNodes = getNakedIds(doc, xPath);
        for (Node node : idNodes) {
            if (!bindingNodes.contains(node) && !"include".equals(node.getNodeName()) &&
                    !"fragment".equals(node.getNodeName())) {
                final Node id = node.getAttributes().getNamedItem("android:id");
                final String className = getViewName(node);
                bundle.createBindingTarget(id.getNodeValue(), className, true, null, null);
            }
        }

        bundle.setBindingClass(getBindingClass(doc, xPath, original));

        return bundle;
    }

    private boolean isBindingLayout(Document doc, XPath xPath) throws XPathExpressionException {
        return !get(doc, xPath, XPATH_BINDING_LAYOUT).isEmpty();
    }

    private Node getLayoutParent(Document doc, XPath xPath) throws XPathExpressionException {
        if (isMerge(doc, xPath)) {
            return get(doc, xPath, XPATH_MERGE_ELEMENT).get(0);
        } else {
            return get(doc, xPath, XPATH_BINDING_LAYOUT).get(0);
        }
    }

    private String getBindingClass(Document doc, XPath xPath, File file)
            throws XPathExpressionException {
        List<Node> nodes = get(doc, xPath, XPATH_BINDING_CLASS);
        if (nodes.isEmpty()) {
            return null;
        }
        if (nodes.size() > 1) {
            L.e("More than one binding class declared in %s", file.getAbsolutePath());
        }
        return nodes.get(0).getNodeValue();
    }

    private List<Node> getBindingNodes(Document doc, XPath xPath) throws XPathExpressionException {
        return get(doc, xPath, XPATH_BINDING_ELEMENTS);
    }

    private List<Node> getVariableNodes(Document doc, XPath xPath) throws XPathExpressionException {
        return get(doc, xPath, XPATH_VARIABLE_DEFINITIONS);
    }

    private List<Node> getImportNodes(Document doc, XPath xPath) throws XPathExpressionException {
        return get(doc, xPath, XPATH_IMPORT_DEFINITIONS);
    }

    private List<Node> getNakedIds(Document doc, XPath xPath) throws XPathExpressionException {
        return get(doc, xPath, XPATH_ID_ELEMENTS);
    }

    private boolean isMerge(Document doc, XPath xPath) throws XPathExpressionException {
        return !get(doc, xPath, XPATH_MERGE_ELEMENT).isEmpty();
    }

    private List<Node> get(Document doc, XPath xPath, String pattern)
            throws XPathExpressionException {
        final XPathExpression expr = xPath.compile(pattern);
        return toList((NodeList) expr.evaluate(doc, XPathConstants.NODESET));
    }

    private List<Node> toList(NodeList nodeList) {
        List<Node> result = new ArrayList<Node>();
        for (int i = 0; i < nodeList.getLength(); i ++) {
            result.add(nodeList.item(i));
        }
        return result;
    }

    private String getViewName(Node viewNode) {
        String viewName = viewNode.getNodeName();
        if ("view".equals(viewName)) {
            Node classNode = viewNode.getAttributes().getNamedItem("class");
            if (classNode == null) {
                L.e("No class attribute for 'view' node");
            } else {
                viewName = classNode.getNodeValue();
            }
        }
        return viewName;
    }

    private void stripBindingTags(File xml, String newTag) throws IOException {
        String res = XmlEditor.INSTANCE$.strip(xml, newTag);
        if (res != null) {
            L.d("file %s has changed, overwriting %s", xml.getName(), xml.getAbsolutePath());
            FileUtils.writeStringToFile(xml, res);
        }
    }

    private File stripFileAndGetOriginal(File xml, String binderId)
            throws ParserConfigurationException, IOException, SAXException,
            XPathExpressionException {
        L.d("parsing resource file %s", xml.getAbsolutePath());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xml);
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        final XPathExpression commentElementExpr = xPath
                .compile("//comment()[starts-with(., \" From: file:\")][last()]");
        final NodeList commentElementNodes = (NodeList) commentElementExpr
                .evaluate(doc, XPathConstants.NODESET);
        L.d("comment element nodes count %s", commentElementNodes.getLength());
        if (commentElementNodes.getLength() == 0) {
            L.d("cannot find comment element to find the actual file");
            return null;
        }
        final Node first = commentElementNodes.item(0);
        String actualFilePath = first.getNodeValue().substring(" From:".length()).trim();
        L.d("actual file to parse: %s", actualFilePath);
        File actualFile = urlToFile(new java.net.URL(actualFilePath));
        if (!actualFile.canRead()) {
            L.d("cannot find original, skipping. %s", actualFile.getAbsolutePath());
            return null;
        }

        // now if file has any binding expressions, find and delete them
        // TODO we should rely on namespace to avoid parsing file twice
        boolean changed = isBindingLayout(doc, xPath);
        if (changed) {
            stripBindingTags(xml, binderId);
        }
        return actualFile;
    }

    public static File urlToFile(String url) throws MalformedURLException {
        return urlToFile(new URL(url));
    }

    public static File urlToFile(URL url) throws MalformedURLException {
        try {
            return new File(url.toURI());
        }
        catch (IllegalArgumentException e) {
            MalformedURLException ex = new MalformedURLException(e.getLocalizedMessage());
            ex.initCause(e);
            throw ex;
        }
        catch (URISyntaxException e) {
            return new File(url.getPath());
        }
    }
}
