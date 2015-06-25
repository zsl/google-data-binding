/*
 * Copyright (C) 2015 The Android Open Source Project
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

package android.databinding.tool.store;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Identifies the range of a code block inside a file or a string.
 * Note that, unlike antlr4 tokens, the line positions start from 0 (to be compatible with Studio).
 * <p>
 * Both start and end line/column indices are inclusive.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Location {
    public static final int NaN = -1;
    @XmlAttribute(name = "startLine")
    public int startLine;
    @XmlAttribute(name = "startOffset")
    public int startOffset;
    @XmlAttribute(name = "endLine")
    public int endLine;
    @XmlAttribute(name = "endOffset")
    public int endOffset;
    @XmlElement
    public Location parentLocation;

    // for XML unmarshalling
    public Location() {
        startOffset = endOffset = startLine = endLine = NaN;
    }

    public Location(Token start, Token end) {
        if (start == null) {
            startLine = startOffset = NaN;
        } else {
            startLine = start.getLine() - 1; //token lines start from 1
            startOffset = start.getCharPositionInLine();
        }

        if (end == null) {
            endLine = endOffset = NaN;
        } else {
            endLine = end.getLine() - 1; // token lines start from 1
            endOffset = end.getCharPositionInLine();
        }
    }

    public Location(ParserRuleContext context) {
        this(context == null ? null : context.getStart(),
                context == null ? null : context.getStop());
    }

    public void setParentLocation(Location parentLocation) {
        this.parentLocation = parentLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Location location = (Location) o;

        if (endLine != location.endLine) {
            return false;
        }
        if (endOffset != location.endOffset) {
            return false;
        }
        if (startLine != location.startLine) {
            return false;
        }
        if (startOffset != location.startOffset) {
            return false;
        }
        if (parentLocation != null ? !parentLocation.equals(location.parentLocation)
                : location.parentLocation != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = startLine;
        result = 31 * result + startOffset;
        result = 31 * result + endLine;
        result = 31 * result + endOffset;
        return result;
    }
}
