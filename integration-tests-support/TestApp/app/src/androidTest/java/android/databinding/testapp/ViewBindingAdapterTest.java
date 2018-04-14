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
package android.databinding.testapp;

import android.content.res.ColorStateList;
import android.databinding.testapp.databinding.ViewAdapterTestBinding;
import android.databinding.testapp.vo.ViewBindingObject;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.test.annotation.UiThreadTest;
import android.view.View;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ViewBindingAdapterTest extends BindingAdapterTestBase<ViewAdapterTestBinding, ViewBindingObject> {

    public ViewBindingAdapterTest() {
        super(ViewAdapterTestBinding.class, ViewBindingObject.class, R.layout.view_adapter_test);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testPadding() {
        View view = mBinder.padding;
        assertEquals(mBindingObject.getPadding(), view.getPaddingBottom());
        assertEquals(mBindingObject.getPadding(), view.getPaddingTop());
        assertEquals(mBindingObject.getPadding(), view.getPaddingRight());
        assertEquals(mBindingObject.getPadding(), view.getPaddingLeft());

        changeValues();

        assertEquals(mBindingObject.getPadding(), view.getPaddingBottom());
        assertEquals(mBindingObject.getPadding(), view.getPaddingTop());
        assertEquals(mBindingObject.getPadding(), view.getPaddingRight());
        assertEquals(mBindingObject.getPadding(), view.getPaddingLeft());
    }

    @Test
    public void testPaddingLeftRight() {
        View view = mBinder.paddingLeftRight;
        assertEquals(mBindingObject.getPaddingLeft(), view.getPaddingLeft());
        assertEquals(mBindingObject.getPaddingRight(), view.getPaddingRight());

        changeValues();

        assertEquals(mBindingObject.getPaddingLeft(), view.getPaddingLeft());
        assertEquals(mBindingObject.getPaddingRight(), view.getPaddingRight());
    }

    @Test
    public void testPaddingStartEnd() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            View view = mBinder.paddingStartEnd;
            assertEquals(mBindingObject.getPaddingStart(), view.getPaddingStart());
            assertEquals(mBindingObject.getPaddingEnd(), view.getPaddingEnd());

            changeValues();

            assertEquals(mBindingObject.getPaddingStart(), view.getPaddingStart());
            assertEquals(mBindingObject.getPaddingEnd(), view.getPaddingEnd());
        }
    }

    @Test
    public void testPaddingTopBottom() {
        View view = mBinder.paddingTopBottom;
        assertEquals(mBindingObject.getPaddingTop(), view.getPaddingTop());
        assertEquals(mBindingObject.getPaddingBottom(), view.getPaddingBottom());

        changeValues();

        assertEquals(mBindingObject.getPaddingTop(), view.getPaddingTop());
        assertEquals(mBindingObject.getPaddingBottom(), view.getPaddingBottom());
    }

    @Test
    public void testBackgroundTint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View view = mBinder.backgroundTint;
            assertNotNull(view.getBackgroundTintList());
            ColorStateList colorStateList = view.getBackgroundTintList();
            assertEquals(mBindingObject.getBackgroundTint(), colorStateList.getDefaultColor());

            changeValues();

            assertNotNull(view.getBackgroundTintList());
            colorStateList = view.getBackgroundTintList();
            assertEquals(mBindingObject.getBackgroundTint(), colorStateList.getDefaultColor());
        }
    }

    @Test
    public void testFadeScrollbars() {
        View view = mBinder.fadeScrollbars;
        assertEquals(mBindingObject.getFadeScrollbars(), view.isScrollbarFadingEnabled());

        changeValues();

        assertEquals(mBindingObject.getFadeScrollbars(), view.isScrollbarFadingEnabled());
    }

    @Test
    public void testNextFocus() {
        View view = mBinder.nextFocus;

        assertEquals(mBindingObject.getNextFocusDown(), view.getNextFocusDownId());
        assertEquals(mBindingObject.getNextFocusUp(), view.getNextFocusUpId());
        assertEquals(mBindingObject.getNextFocusLeft(), view.getNextFocusLeftId());
        assertEquals(mBindingObject.getNextFocusRight(), view.getNextFocusRightId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            assertEquals(mBindingObject.getNextFocusForward(), view.getNextFocusForwardId());
        }

        changeValues();

        assertEquals(mBindingObject.getNextFocusDown(), view.getNextFocusDownId());
        assertEquals(mBindingObject.getNextFocusUp(), view.getNextFocusUpId());
        assertEquals(mBindingObject.getNextFocusLeft(), view.getNextFocusLeftId());
        assertEquals(mBindingObject.getNextFocusRight(), view.getNextFocusRightId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            assertEquals(mBindingObject.getNextFocusForward(), view.getNextFocusForwardId());
        }
    }

    @Test
    public void testRequiresFadingEdge() {
        View view = mBinder.requiresFadingEdge;

        assertTrue(view.isVerticalFadingEdgeEnabled());
        assertFalse(view.isHorizontalFadingEdgeEnabled());

        changeValues();

        assertFalse(view.isVerticalFadingEdgeEnabled());
        assertTrue(view.isHorizontalFadingEdgeEnabled());
    }

    @Test
    public void testScrollbar() {
        View view = mBinder.scrollbar;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            assertEquals(mBindingObject.getScrollbarDefaultDelayBeforeFade(),
                    view.getScrollBarDefaultDelayBeforeFade());
            assertEquals(mBindingObject.getScrollbarFadeDuration(), view.getScrollBarFadeDuration());
            assertEquals(mBindingObject.getScrollbarSize(), view.getScrollBarSize());
        }
        assertEquals(mBindingObject.getScrollbarStyle(), view.getScrollBarStyle());

        changeValues();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            assertEquals(mBindingObject.getScrollbarDefaultDelayBeforeFade(),
                    view.getScrollBarDefaultDelayBeforeFade());
            assertEquals(mBindingObject.getScrollbarFadeDuration(), view.getScrollBarFadeDuration());
            assertEquals(mBindingObject.getScrollbarSize(), view.getScrollBarSize());
        }
        assertEquals(mBindingObject.getScrollbarStyle(), view.getScrollBarStyle());
    }

    @Test
    public void testTransformPivot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            View view = mBinder.transformPivot;

            assertEquals(mBindingObject.getTransformPivotX(), view.getPivotX(), 0f);
            assertEquals(mBindingObject.getTransformPivotY(), view.getPivotY(), 0f);

            changeValues();

            assertEquals(mBindingObject.getTransformPivotX(), view.getPivotX(), 0f);
            assertEquals(mBindingObject.getTransformPivotY(), view.getPivotY(), 0f);
        }
    }

    @Test
    @UiThreadTest
    public void testBackgroundDrawableDrawable() {
        View view = mBinder.backgroundDrawable;
        Drawable drawable = view.getBackground();
        assertNotNull(drawable);
    }

    @Test
    @UiThreadTest
    public void testBackgroundDrawableWithTheme() {
        View view = mBinder.backgroundWithTheme;
        assertNotNull(view.getBackground());
    }
}
