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

import java.io.File
import org.antlr.v4.runtime.ANTLRInputStream
import java.io.FileReader
import android.databinding.parser.XMLLexer
import org.antlr.v4.runtime.CommonTokenStream
import android.databinding.parser.XMLParser
import android.databinding.parser.log
import android.databinding.parser.XMLParserBaseVisitor
import android.databinding.parser.Position
import android.databinding.parser.toPosition
import android.databinding.parser.toEndPosition
import java.util.Comparator
import com.google.common.base.Preconditions
import java.util.ArrayList

/**
 * Ugly inefficient class to strip unwanted tags from XML.
 * Band-aid solution to unblock development
 */
object XmlEditor {
    val reservedElementNames = arrayListOf("variable", "import")
    var rootNodeContext: XMLParser.ElementContext? = null
    var rootNodeHasTag = false
    var isRootNode = false
    data class LayoutXmlElements(val start: Position, val end: Position,
                                 val insertionPoint: Position, val name : String,
                                 val isTag: kotlin.Boolean, val isReserved: kotlin.Boolean,
                                 val attributes: MutableList<LayoutXmlElements>?,
                                 val children: MutableList<LayoutXmlElements>,
                                 val isRoot: kotlin.Boolean)

    val visitor = object : XMLParserBaseVisitor<MutableList<LayoutXmlElements>>() {

        override fun visitAttribute(ctx: XMLParser.AttributeContext): MutableList<LayoutXmlElements>? {
            log { "attr:${ctx.attrName.getText()} ${ctx.attrValue.getText()} . parent: ${ctx.getParent()}" }
            if (ctx.getParent() == rootNodeContext && ctx.attrName.getText() == "android:tag") {
                rootNodeHasTag = true
            }
            val isTag = ctx.attrName.getText().equals("android:tag")
            if (isTag || ctx.attrName.getText().startsWith("bind:") ||
                    (ctx.attrValue.getText().startsWith("\"@{") && ctx.attrValue.getText().endsWith("}\"")) ||
                    (ctx.attrValue.getText().startsWith("'@{") && ctx.attrValue.getText().endsWith("}'"))) {
                return arrayListOf(LayoutXmlElements(ctx.getStart().toPosition(),
                    ctx.getStop().toEndPosition(), ctx.getStart().toPosition(),
                        ctx.attrName.getText(), isTag, false, null, arrayListOf(), false))
            }

            //log{"visiting attr: ${ctx.getText()} at location ${ctx.getStart().toS()} ${ctx.getStop().toS()}"}
            return super<XMLParserBaseVisitor>.visitAttribute(ctx)
        }

        override fun visitElement(ctx: XMLParser.ElementContext): MutableList<LayoutXmlElements>? {
            log { "elm ${ctx.elmName.getText()} || ${ctx.Name()} paren : ${ctx.getParent()} isRoot : ${isRootNode}" }
            var wasRoot = isRootNode
            if (rootNodeContext == null) {
                rootNodeContext = ctx
                if ("merge".equals(ctx.elmName.getText())) {
                    isRootNode = true
                } else {
                    wasRoot = true;
                }
            } else {
                isRootNode = false
            }
            val insertionPoint : Position
            if (ctx.content() == null) {
                insertionPoint = ctx.getStop().toPosition()
                insertionPoint.charIndex;
            } else {
                insertionPoint = ctx.content().getStart().toPosition()
                insertionPoint.charIndex -= 1;
            }
            if (reservedElementNames.contains(ctx.elmName?.getText()) || ctx.elmName.getText().startsWith("bind:")) {
                isRootNode = wasRoot
                return arrayListOf(LayoutXmlElements(ctx.getStart().toPosition(),
                        ctx.getStop().toEndPosition(), insertionPoint, ctx.elmName.getText(), false,
                        true, arrayListOf(), arrayListOf(), false));
            }
            val children = arrayListOf<LayoutXmlElements>();
            val attributes = arrayListOf<LayoutXmlElements>();
            val element = LayoutXmlElements(ctx.getStart().toPosition(),
                    ctx.getStop().toEndPosition(), insertionPoint, ctx.elmName.getText(), false,
                    false, attributes, children, wasRoot)
            val elements = super<XMLParserBaseVisitor>.visitElement(ctx);
            isRootNode = wasRoot
            if (elements != null && !elements.isEmpty()) {
                elements.forEach {
                    if (it.attributes == null) {
                        attributes.add(it);
                    } else {
                        children.add(it);
                    }
                }
                elements.removeAll(attributes);
            }
            elements.add(element)
            return elements;
        }

        override fun defaultResult(): MutableList<LayoutXmlElements>? = arrayListOf()

        override fun aggregateResult(aggregate: MutableList<LayoutXmlElements>?, nextResult: MutableList<LayoutXmlElements>?): MutableList<LayoutXmlElements>? =
                if (aggregate == null) {
                    nextResult
                } else if (nextResult == null) {
                    aggregate
                } else {
                    aggregate.addAll(nextResult)
                    aggregate
                }
    }

    fun containsInclude(node : LayoutXmlElements) =
        node.children.firstOrNull(){ "include".equals(it.name) } != null

    fun strip(f: File, newTag: String? = null): String? {
        rootNodeContext = null //clear it
        rootNodeHasTag = false
        isRootNode = false;
        val inputStream = ANTLRInputStream(FileReader(f))
        val lexer = XMLLexer(inputStream)
        val tokenStream = CommonTokenStream(lexer)
        val parser = XMLParser(tokenStream)
        val expr = parser.document()
        val parsedExpr = expr.accept(visitor)
        if (parsedExpr.isEmpty()) {
            return null//nothing to strip
        }
        Preconditions.checkNotNull(rootNodeContext, "Cannot find root node for ${f.getName()}")
        Preconditions.checkState(rootNodeHasTag == false, """You cannot set a tag in the layout
        root if you are using binding. Invalid file: ${f}""")
        val rootNodeBounds = Pair(rootNodeContext!!.getStart().toPosition(),
                rootNodeContext!!.getStop().toEndPosition())

        log { "root node bounds: ${rootNodeBounds}" }
        val out = StringBuilder()
        val lines = f.readLines("utf-8")

        lines.forEach { out.appendln(it) }

        // TODO we probably don't need to sort
        val sorted = parsedExpr.sortBy(object : Comparator<LayoutXmlElements> {
            override fun compare(o1: LayoutXmlElements, o2: LayoutXmlElements): Int {
                val lineCmp = o1.start.line.compareTo(o2.start.charIndex)
                if (lineCmp != 0) {
                    return lineCmp
                }
                return o1.start.line.compareTo(o2.start.charIndex)
            }
        })
        var lineStarts = arrayListOf(0)
        lines.withIndex().forEach {
            if (it.index > 0) {
                lineStarts.add(lineStarts[it.index - 1] + lines[it.index - 1].length() + 1)
            }
        }

        val separator = System.lineSeparator().charAt(0)
        val noTag : ArrayList<Pair<String, LayoutXmlElements>> = ArrayList()
        var bindingIndex = 1
        var rootDone = false
        sorted.forEach {
            if (it.isReserved) {
                log {"Replacing reserved tag at ${it.start} to ${it.end}"}
                replace(out, lineStarts, it, "", separator);
            } else if ((it.attributes != null && !it.attributes.isEmpty()) || containsInclude(it) ||
                    it.isRoot) {
                log {"Found attribute for tag at ${it.start} to ${it.end}"}
                if (it.attributes != null && it.attributes.size() == 1 &&
                        it.attributes.get(0).isTag) {
                    log {"only android:tag"}
                    // no binding, just tag -- don't replace anything
                } else {
                    var replaced = false
                    val tag : String
                    if (it.isRoot) {
                        val index : kotlin.Int
                        if (rootDone) {
                            index = bindingIndex++
                        } else {
                            index = 0
                            rootDone = true
                        }
                        tag = "android:tag=\"${newTag}_${index}\""
                    } else if ("include".equals(it.name)) {
                        tag = ""
                    } else {
                        val index = bindingIndex++;
                        tag = "android:tag=\"binding_${index}\"";
                    }
                    it.attributes?.forEach {
                        if (!replaced && tagWillFit(it.start, it.end, tag)) {
                            replace(out, lineStarts, it, tag, separator)
                            replaced = true;
                        } else {
                            replace(out, lineStarts, it, "", separator)
                        }
                    }
                    if (!replaced && !tag.isEmpty()) {
                        log {"Could not find place for ${tag}"}
                        noTag.add(Pair(tag, it))
                    }
                }
            }
        }

        noTag.sortBy(object : Comparator<Pair<String, LayoutXmlElements>> {
            override fun compare(o1: Pair<String, LayoutXmlElements>, o2: Pair<String, LayoutXmlElements>): Int {
                val lineCmp = o2.second.start.line.compareTo(o1.second.start.charIndex)
                if (lineCmp != 0) {
                    return lineCmp
                }
                return o2.second.start.line.compareTo(o1.second.start.charIndex)
            }
        }).forEach {
            val element = it.second
            val tag = it.first;

            val insertionPoint = toIndex(lineStarts, element.insertionPoint)
            log { "insert tag '${tag}' at ${insertionPoint} line : ${element.insertionPoint.line} column : ${element.insertionPoint.charIndex}"}
            out.insert(insertionPoint, " ${tag}")
        }

        return out.toString()
    }

    fun tagWillFit(start: Position, end: Position, tag: String) : kotlin.Boolean {
        if (start.line != end.line) {
            return end.charIndex >= tag.length();
        }
        return (end.charIndex - start.charIndex >= tag.length());
    }

    fun replace(out : StringBuilder, lineStarts : ArrayList<kotlin.Int>, element : LayoutXmlElements,
                tag : String, separator : kotlin.Char) {
        val posStart = toIndex(lineStarts, element.start)
        val posEnd = toIndex(lineStarts, element.end)
        log {"replacing '${out.substring(posStart, posEnd)}' with '${tag}'"}
        val spaceEnd = posEnd - tag.length();
        for ( i in posStart..(spaceEnd - 1)) {
            if (out.charAt(i) != separator) {
                out.setCharAt(i, ' ')
            }
        }
        out.replace(spaceEnd, posEnd, tag);
    }

    fun toIndex(lineStarts : ArrayList<kotlin.Int>, pos : Position) : kotlin.Int {
        return lineStarts[pos.line] + pos.charIndex;
    }
}
