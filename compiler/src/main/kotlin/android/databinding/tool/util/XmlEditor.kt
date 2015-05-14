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

package android.databinding.tool.util

import android.databinding.parser.BindingExpressionLexer
import android.databinding.parser.BindingExpressionParser
import android.databinding.parser.Position
import android.databinding.parser.XMLParser
import android.databinding.parser.toEndPosition
import android.databinding.parser.toPosition
import android.databinding.parser.XMLLexer
import java.io.File
import org.antlr.v4.runtime.ANTLRInputStream
import java.io.FileReader
import org.antlr.v4.runtime.CommonTokenStream
import java.util.Comparator
import com.google.common.base.Preconditions
import org.apache.commons.lang3.StringEscapeUtils
import java.util.ArrayList
import java.util.regex.Pattern

/**
 * Ugly inefficient class to strip unwanted tags from XML.
 * Band-aid solution to unblock development
 */
object XmlEditor {
    fun XMLParser.ElementContext.nodeName() = this.elmName.getText()

    fun XMLParser.ElementContext.attributes() =
            if (attribute() == null) {
                arrayListOf<XMLParser.AttributeContext>()
            } else {
                attribute()
            }

    fun XMLParser.ElementContext.hasExpressionAttributes() : kotlin.Boolean {
        val expressions = expressionAttributes()
        return expressions.size() > 1 || (expressions.size() == 1 &&
                !expressions.get(0).attrName.getText().equals("android:tag"))
    }

    fun XMLParser.ElementContext.expressionAttributes() = attributes().filter {
        val attrName = it.attrName.getText();
        val value = it.attrValue.getText()
        attrName.equals("android:tag") ||
                (value.startsWith("\"@{") && value.endsWith("}\"")) ||
                (value.startsWith("\'@{") && value.endsWith("}\'"))
    }

    fun XMLParser.ElementContext.endTagPosition(): Position {
        if (content() == null) {
            // no content, so just subtract from the "/>"
            val endTag = getStop().toEndPosition()
            Preconditions.checkState(endTag.charIndex > 0)
            endTag.charIndex -= 2
            return endTag
        } else {
            // tag with no attributes, but with content
            val position = content().getStart().toPosition()
            Preconditions.checkState(position.charIndex > 0)
            position.charIndex--
            return position
        }
    }

    fun XMLParser.ElementContext.elements() : List<XMLParser.ElementContext> {
        if (content() != null && content().element() != null) {
            return content().element()
        } else {
            return arrayListOf<XMLParser.ElementContext>()
        }
    }

    fun strip(f: File, newTag: String? = null): String? {
        val inputStream = ANTLRInputStream(FileReader(f))
        val lexer = XMLLexer(inputStream)
        val tokenStream = CommonTokenStream(lexer)
        val parser = XMLParser(tokenStream)
        val expr = parser.document()
        val root = expr.element()

        if (root == null || !"layout".equals(root.nodeName())) {
            return null; // not a binding layout
        }

        val dataNodes = root.elements().filter { "data".equals(it.nodeName()) }
        Preconditions.checkState(dataNodes.size() < 2,
                "Multiple binding data tags. Expecting a maximum of one.");

        val lines = arrayListOf<String>()
        lines.addAll(f.readLines("utf-8"))

        dataNodes.forEach {
            replace(lines, it.getStart().toPosition(), it.getStop().toEndPosition(), "")
        }

        val layoutNodes = root.elements().filter { !"data".equals(it.nodeName()) }
        Preconditions.checkState(layoutNodes.size() == 1,
                "Only one layout element and one data element are allowed")

        val layoutNode = layoutNodes.get(0)

        val noTag = arrayListOf<Pair<String, XMLParser.ElementContext>>()

        recurseReplace(layoutNode, lines, noTag, newTag, 0)

        // Remove the <layout>
        val rootStartTag = root.getStart().toPosition()
        val rootEndTag = root.content().getStart().toPosition();
        replace(lines, rootStartTag, rootEndTag, "")

        // Remove the </layout>
        val endLayoutPositions = findTerminalPositions(root, lines)
        replace(lines, endLayoutPositions.first, endLayoutPositions.second, "")

        val rootAttributes = StringBuilder()
        root.attributes().fold(rootAttributes) {
            str : StringBuilder, attr -> str.append(' ').append(attr.getText())
        }

        val noTagRoot = noTag.firstOrNull() { it.second == layoutNode }
        if (noTagRoot != null) {
            val newRootTag = Pair(noTagRoot.first + rootAttributes.toString(), layoutNode)
            val index = noTag.indexOf(noTagRoot)
            noTag.set(index, newRootTag)
        } else {
            val newRootTag = Pair(rootAttributes.toString(), layoutNode)
            noTag.add(newRootTag)
        }

        noTag.sortBy(object : Comparator<Pair<String, XMLParser.ElementContext>> {
            override fun compare(o1: Pair<String, XMLParser.ElementContext>,
                    o2: Pair<String, XMLParser.ElementContext>): Int {
                val start1 = o1.second.getStart().toPosition()
                val start2 = o2.second.getStart().toPosition()
                val lineCmp = start2.line.compareTo(start1.line)
                if (lineCmp != 0) {
                    return lineCmp
                }
                return start2.charIndex.compareTo(start2.charIndex)
            }
        }).forEach {
            val element = it.second
            val tag = it.first;
            val endTagPosition = element.endTagPosition()
            fixPosition(lines, endTagPosition)
            val line = lines.get(endTagPosition.line)
            val newLine = line.substring(0, endTagPosition.charIndex) +
                " ${tag}" + line.substring(endTagPosition.charIndex)
            lines.set(endTagPosition.line, newLine)
        }

        return lines.fold(StringBuilder()) { sb, line ->
            sb.append(line).append(System.getProperty("line.separator"))
        }.toString()
    }

    fun findTerminalPositions(node : XMLParser.ElementContext, lines : ArrayList<String>) :
            Pair<Position, Position> {
        val endPosition = node.getStop().toEndPosition()
        val startPosition = node.getStop().toPosition()
        var index : kotlin.Int
        do {
            index = lines.get(startPosition.line).lastIndexOf("</")
            startPosition.line--
        } while (index < 0)
        startPosition.line++
        startPosition.charIndex = index
        return Pair(startPosition, endPosition)
    }

    fun recurseReplace(node : XMLParser.ElementContext, lines : ArrayList<String>,
            noTag : ArrayList<Pair<String, XMLParser.ElementContext>>,
            newTag : String?, bindingIndex : kotlin.Int) : kotlin.Int {
        var nextBindingIndex = bindingIndex
        val isMerge = "merge".equals(node.nodeName())
        if (!isMerge && (node.hasExpressionAttributes() || newTag != null)) {
            var tag = ""
            if (newTag != null) {
                tag = "android:tag=\"${newTag}_${bindingIndex}\""
                nextBindingIndex++
            } else if (!"include".equals(node.nodeName())) {
                tag = "android:tag=\"binding_${bindingIndex}\""
                nextBindingIndex++
            }
            node.expressionAttributes().forEach {
                val start = it.getStart().toPosition()
                val end = it.getStop().toEndPosition()
                val defaultVal = defaultReplacement(it)
                if (defaultVal != null) {
                    replace(lines, start, end, "${it.attrName.getText()}=\"${defaultVal}\"")
                } else if (replace(lines, start, end, tag)) {
                    tag = ""
                }
            }
            if (tag.length() != 0) {
                noTag.add(Pair(tag, node))
            }
        }

        val nextTag : String?
        if (bindingIndex == 0 && isMerge) {
            nextTag = newTag
        } else {
            nextTag = null
        }
        node.elements().forEach {
            nextBindingIndex = recurseReplace(it, lines, noTag, nextTag, nextBindingIndex)
        }
        return nextBindingIndex
    }

    fun defaultReplacement(attr : XMLParser.AttributeContext) : String? {
        val textWithQuotes = attr.attrValue.getText()
        val escapedText = textWithQuotes.substring(1, textWithQuotes.length() - 1)
        if (!escapedText.startsWith("@{") || !escapedText.endsWith("}")) {
            return null;
        }
        val text = StringEscapeUtils.unescapeXml(escapedText.substring(2, escapedText.length() - 1))
        val inputStream = ANTLRInputStream(text)
        val lexer = BindingExpressionLexer(inputStream)
        val tokenStream = CommonTokenStream(lexer)
        val parser = BindingExpressionParser(tokenStream)
        val root = parser.bindingSyntax()
        val defaults = root.defaults()
        if (defaults != null) {
            val constantValue = defaults.constantValue()
            val literal = constantValue.literal()
            if (literal != null) {
                val stringLiteral = literal.stringLiteral()
                if (stringLiteral != null) {
                    val doubleQuote = stringLiteral.DoubleQuoteString()
                    if (doubleQuote != null) {
                        val quotedStr = doubleQuote.getText()
                        val unquoted = quotedStr.substring(1, quotedStr.length() - 1)
                        return StringEscapeUtils.escapeXml10(unquoted)
                    } else {
                        val quotedStr = stringLiteral.SingleQuoteString().getText()
                        val unquoted = quotedStr.substring(1, quotedStr.length() - 1)
                        val unescaped = unquoted.replace("\"", "\\\"").replace("\\`", "`")
                        return StringEscapeUtils.escapeXml10(unescaped)
                    }
                }
            }
            return constantValue.getText()
        }
        return null
    }

    fun replace(lines : ArrayList<String>, start: Position, end: Position, text: String) :
            kotlin.Boolean {
        fixPosition(lines, start)
        fixPosition(lines, end)
        if (start.line != end.line) {
            val startLine = lines.get(start.line)
            val newStartLine = startLine.substring(0, start.charIndex) +
                    text;
            lines.set(start.line, newStartLine)
            for (i in start.line + 1 .. end.line - 1) {
                val line = lines.get(i)
                lines.set(i, replaceWithSpaces(line, 0, line.length() - 1))
            }
            val endLine = lines.get(end.line)
            val newEndLine = replaceWithSpaces(endLine, 0, end.charIndex - 1)
            lines.set(end.line, newEndLine)
            return true
        } else if (end.charIndex - start.charIndex >= text.length()) {
            val line = lines.get(start.line)
            val endTextIndex = start.charIndex + text.length()
            val replacedText = line.replaceRange(start.charIndex, endTextIndex, text)
            val spacedText = replaceWithSpaces(replacedText, endTextIndex, end.charIndex - 1)
            lines.set(start.line, spacedText)
            return true
        } else {
            val line = lines.get(start.line)
            val newLine = replaceWithSpaces(line, start.charIndex, end.charIndex - 1)
            lines.set(start.line, newLine)
            return false;
        }
    }

    fun replaceWithSpaces(line : String, start : kotlin.Int, end : kotlin.Int) : String {
        val lineBuilder = StringBuilder(line)
        for (i in start..end) {
            lineBuilder.setCharAt(i, ' ')
        }
        return lineBuilder.toString()
    }

    fun fixPosition(lines : ArrayList<String>, pos : Position) {
        val line = lines.get(pos.line)
        while (pos.charIndex > line.length()) {
            pos.charIndex--
        }
    }
}
