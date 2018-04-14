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
import android.databinding.testapp.vo.User;
import android.support.test.annotation.UiThreadTest;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.databinding.testapp.vo.ObservableFieldBindingObject.MyParcelable;
import static java.lang.String.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ObservableFieldTest extends BaseDataBinderTest<ObservableFieldTestBinding> {
    private ObservableFieldBindingObject mObj;

    public ObservableFieldTest() {
        super(ObservableFieldTestBinding.class);
    }

    @Override
    public void setUp() throws Exception {
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

    @Test
    @UiThreadTest
    public void testBoolean() {
        assertEquals("false", mBinder.bField.getText());
//        assertEquals("true", mBinder.bDep.getText());

        mObj.bField.set(true);
        mBinder.executePendingBindings();

        assertEquals("true", mBinder.bField.getText());
//        assertEquals("false", mBinder.bDep.getText());
    }

    @Test
    @UiThreadTest
    public void testByte() {
        assertEquals("0", mBinder.tField.getText());
//        assertEquals("1", mBinder.tDep.getText());

        mObj.tField.set((byte) 1);
        mBinder.executePendingBindings();

        assertEquals("1", mBinder.tField.getText());
//        assertEquals("2", mBinder.tDep.getText());
    }

    @Test
    @UiThreadTest
    public void testShort() {
        assertEquals("0", mBinder.sField.getText());
//        assertEquals("1", mBinder.sDep.getText());

        mObj.sField.set((short) 1);
        mBinder.executePendingBindings();

        assertEquals("1", mBinder.sField.getText());
//        assertEquals("2", mBinder.sDep.getText());
    }

    @Test
    @UiThreadTest
    public void testChar() {
        assertEquals("\u0000", mBinder.cField.getText());
//        assertEquals("+", mBinder.cDep.getText());

        mObj.cField.set('+');
        mBinder.executePendingBindings();

        assertEquals("+", mBinder.cField.getText());
//        assertEquals("-", mBinder.cDep.getText());
    }

    @Test
    @UiThreadTest
    public void testInt() {
        assertEquals("0", mBinder.iField.getText());
//        assertEquals("1", mBinder.iDep.getText());

        mObj.iField.set(1);
        mBinder.executePendingBindings();

        assertEquals("1", mBinder.iField.getText());
//        assertEquals("2", mBinder.iDep.getText());
    }

    @Test
    @UiThreadTest
    public void testLong() {
        assertEquals("0", mBinder.lField.getText());
//        assertEquals("1", mBinder.lDep.getText());

        mObj.lField.set(1);
        mBinder.executePendingBindings();

        assertEquals("1", mBinder.lField.getText());
//        assertEquals("2", mBinder.lDep.getText());
    }

    @Test
    @UiThreadTest
    public void testFloat() {
        assertEquals("0.0", mBinder.fField.getText());
//        assertEquals("1.0", mBinder.fDep.getText());

        mObj.fField.set(1);
        mBinder.executePendingBindings();

        assertEquals("1.0", mBinder.fField.getText());
//        assertEquals("2.0", mBinder.fDep.getText());
    }

    @Test
    @UiThreadTest
    public void testDouble() {
        assertEquals("0.0", mBinder.dField.getText());
//        assertEquals("1.0", mBinder.dDep.getText());

        mObj.dField.set(1);
        mBinder.executePendingBindings();

        assertEquals("1.0", mBinder.dField.getText());
//        assertEquals("2.0", mBinder.dDep.getText());
    }

    @Test
    @UiThreadTest
    public void testObject() {
        assertEquals("Hello", mBinder.oField.getText());
//        assertEquals("Hello dependency", mBinder.oDep.getText());

        mObj.oField.set("World");
        mBinder.executePendingBindings();

        assertEquals("World", mBinder.oField.getText());
//        assertEquals("World dependency", mBinder.oDep.getText());
    }

    @Test
    @UiThreadTest
    public void testParcelable() {
        TextView x = mBinder.pFieldx;
        TextView y = mBinder.pFieldy;
        assertEquals(x.getText().toString(), valueOf(mObj.pField.get().getX()));
        assertEquals(y.getText().toString(), mObj.pField.get().getY());
        MyParcelable p2 =
                new MyParcelable(7, "updated");
        mObj.pField.set(p2);
        mBinder.executePendingBindings();

        assertEquals(x.getText().toString(), valueOf(mObj.pField.get().getX()));
        assertEquals(y.getText().toString(), mObj.pField.get().getY());
    }

    @Test
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

    @Test
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

    @Test
    @UiThreadTest
    public void testModelObject() {
        TextView view = mBinder.mFieldModel;
        assertEquals("name", view.getText());

        User newUser = new User();
        newUser.setName("new name");
        mObj.mField.set(newUser);
        mBinder.executePendingBindings();

        assertEquals("new name", view.getText());
    }

    @Test
    @UiThreadTest
    public void testModelProperty() {
        TextView view = mBinder.mFieldModel;
        assertEquals("name", view.getText());

        mObj.mField.get().setName("change name");
        mBinder.executePendingBindings();

        assertEquals("change name", view.getText());
    }

    @Test
    @UiThreadTest
    public void testNestedModelObject() {
        TextView view = mBinder.mFieldNestedModel;
        assertEquals("friend name", view.getText());

        User newFriend = new User();
        newFriend.setName("new friend name");
        mObj.mField.get().setFriend(newFriend);
        mBinder.executePendingBindings();

        assertEquals("new friend name", view.getText());
    }

    @Test
    @UiThreadTest
    public void testNestedModelProperty() {
        TextView view = mBinder.mFieldNestedModel;
        assertEquals("friend name", view.getText());

        mObj.mField.get().getFriend().setName("change friend name");
        mBinder.executePendingBindings();

        assertEquals("change friend name", view.getText());
    }

    @Test
    @UiThreadTest
    public void testObjectParameter() {
        TextView view = mBinder.observableCast;
        mBinder.executePendingBindings();
        assertEquals("false", view.getText());

        ObservableBoolean enabled = new ObservableBoolean(false);
        mBinder.setEnabled(enabled);
        mBinder.executePendingBindings();
        assertEquals("false", view.getText());

        enabled.set(true);
        mBinder.executePendingBindings();
        assertEquals("true", view.getText());
    }

    @Test
    @UiThreadTest
    public void testArgvParameter() {
        TextView view = mBinder.observableCast2;
        mBinder.executePendingBindings();
        assertEquals("false", view.getText());

        ObservableBoolean enabled = new ObservableBoolean(false);
        mBinder.setEnabled(enabled);
        mBinder.executePendingBindings();
        assertEquals("false", view.getText());

        enabled.set(true);
        mBinder.executePendingBindings();
        assertEquals("true", view.getText());
    }

    @Test
    @UiThreadTest
    public void testObservableArgvParameter() {
        TextView view = mBinder.observableCast3;
        mBinder.executePendingBindings();
        assertEquals("false", view.getText());

        ObservableBoolean enabled = new ObservableBoolean(false);
        mBinder.setEnabled(enabled);
        mBinder.executePendingBindings();
        assertEquals("false", view.getText());

        enabled.set(true);
        mBinder.executePendingBindings();
        assertEquals("true", view.getText());
    }

    /**
     * TODO: This should disappear in Android Studio 2.4 after this capapbility has been removed.
     */
    @Test
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
