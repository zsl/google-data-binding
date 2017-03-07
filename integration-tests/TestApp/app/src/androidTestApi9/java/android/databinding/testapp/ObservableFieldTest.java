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

package android.databinding.testapp;

import android.databinding.ObservableBoolean;
import android.databinding.testapp.databinding.ObservableFieldTestBinding;
import android.databinding.testapp.vo.ObservableFieldBindingObject;

import android.test.UiThreadTest;
import android.widget.TextView;

public class ObservableFieldTest extends BaseDataBinderTest<ObservableFieldTestBinding> {
    private ObservableFieldBindingObject mObj;

    public ObservableFieldTest() {
        super(ObservableFieldTestBinding.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initBinder(new Runnable() {
            @Override
            public void run() {
                mObj = new ObservableFieldBindingObject();
                mBinder.setObj(mObj);
                mBinder.executePendingBindings();
            }
        });
    }

    @UiThreadTest
    public void testBoolean() {
        assertEquals("false", mBinder.bField.getText());
        assertEquals("true", mBinder.bDep.getText());

        mObj.bField.set(true);
        mBinder.executePendingBindings();

        assertEquals("true", mBinder.bField.getText());
        assertEquals("false", mBinder.bDep.getText());
    }

    @UiThreadTest
    public void testByte() {
        assertEquals("0", mBinder.tField.getText());
        assertEquals("1", mBinder.tDep.getText());

        mObj.tField.set((byte) 1);
        mBinder.executePendingBindings();

        assertEquals("1", mBinder.tField.getText());
        assertEquals("2", mBinder.tDep.getText());
    }

    @UiThreadTest
    public void testShort() {
        assertEquals("0", mBinder.sField.getText());
        assertEquals("1", mBinder.sDep.getText());

        mObj.sField.set((short) 1);
        mBinder.executePendingBindings();

        assertEquals("1", mBinder.sField.getText());
        assertEquals("2", mBinder.sDep.getText());
    }

    @UiThreadTest
    public void testChar() {
        assertEquals("\u0000", mBinder.cField.getText());
        assertEquals("+", mBinder.cDep.getText());

        mObj.cField.set('+');
        mBinder.executePendingBindings();

        assertEquals("+", mBinder.cField.getText());
        assertEquals("-", mBinder.cDep.getText());
    }

    @UiThreadTest
    public void testInt() {
        assertEquals("0", mBinder.iField.getText());
        assertEquals("1", mBinder.iDep.getText());

        mObj.iField.set(1);
        mBinder.executePendingBindings();

        assertEquals("1", mBinder.iField.getText());
        assertEquals("2", mBinder.iDep.getText());
    }

    @UiThreadTest
    public void testLong() {
        assertEquals("0", mBinder.lField.getText());
        assertEquals("1", mBinder.lDep.getText());

        mObj.lField.set(1);
        mBinder.executePendingBindings();

        assertEquals("1", mBinder.lField.getText());
        assertEquals("2", mBinder.lDep.getText());
    }

    @UiThreadTest
    public void testFloat() {
        assertEquals("0.0", mBinder.fField.getText());
        assertEquals("1.0", mBinder.fDep.getText());

        mObj.fField.set(1);
        mBinder.executePendingBindings();

        assertEquals("1.0", mBinder.fField.getText());
        assertEquals("2.0", mBinder.fDep.getText());
    }

    @UiThreadTest
    public void testDouble() {
        assertEquals("0.0", mBinder.dField.getText());
        assertEquals("1.0", mBinder.dDep.getText());

        mObj.dField.set(1);
        mBinder.executePendingBindings();

        assertEquals("1.0", mBinder.dField.getText());
        assertEquals("2.0", mBinder.dDep.getText());
    }

    @UiThreadTest
    public void testObject() {
        assertEquals("Hello", mBinder.oField.getText());
        assertEquals("Hello dependency", mBinder.oDep.getText());

        mObj.oField.set("World");
        mBinder.executePendingBindings();

        assertEquals("World", mBinder.oField.getText());
        assertEquals("World dependency", mBinder.oDep.getText());
    }

    @UiThreadTest
    public void testParcelable() {
        TextView x = mBinder.pFieldx;
        TextView y = mBinder.pFieldy;
        assertEquals(x.getText().toString(), String.valueOf(mObj.pField.get().getX()));
        assertEquals(y.getText().toString(), mObj.pField.get().getY());
        ObservableFieldBindingObject.MyParcelable p2 =
                new ObservableFieldBindingObject.MyParcelable(7, "updated");
        mObj.pField.set(p2);
        mBinder.executePendingBindings();

        assertEquals(x.getText().toString(), String.valueOf(mObj.pField.get().getX()));
        assertEquals(y.getText().toString(), mObj.pField.get().getY());
    }

    @UiThreadTest
    public void testObservableVariables() {
        ObservableBoolean enabled = new ObservableBoolean(false);
        mBinder.setEnabled(enabled);
        mBinder.executePendingBindings();
        assertFalse(mBinder.enabledView.isEnabled());
        enabled.set(true);
        mBinder.executePendingBindings();
        assertTrue(mBinder.enabledView.isEnabled());
    }

    @UiThreadTest
    public void testNestedObservables() {
        ObservableBoolean enabled = new ObservableBoolean(false);
        mBinder.setEnabled(enabled);
        mBinder.executePendingBindings();
        assertFalse(mBinder.nestedObservableView.isEnabled());
        assertEquals(mObj.oField.get(), mBinder.nestedObservableView.getText().toString());

        enabled.set(true);
        mObj.oField.set("Blah");
        mBinder.executePendingBindings();
        assertTrue(mBinder.nestedObservableView.isEnabled());
        assertEquals(mObj.oField.get(), mBinder.nestedObservableView.getText().toString());
    }

    /**
     * TODO: This should disappear in Android Studio 2.4 after this capapbility has been removed.
     */
    @UiThreadTest
    public void testObservableGet() {
        ObservableBoolean enabled = new ObservableBoolean(false);
        mBinder.setEnabled(enabled);
        mBinder.executePendingBindings();
        assertFalse(mBinder.useGet.isEnabled());
        enabled.set(true);
        mBinder.executePendingBindings();
        assertTrue(mBinder.useGet.isEnabled());
    }
}
