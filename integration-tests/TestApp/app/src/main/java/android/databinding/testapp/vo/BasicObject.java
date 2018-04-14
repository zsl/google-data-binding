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

package android.databinding.testapp.vo;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableInt;
import android.databinding.testapp.BR;

public class BasicObject extends BaseObservable {
    public final ObservableInt val = new ObservableInt();

    @Bindable
    private String mField1;
    @Bindable
    private String mField2;

    @Bindable
    public String field3;

    @Bindable("field3")
    private String mField4 = "+";

    private String mJoin = " ";

    private String mField5 = "boo";

    public String getField1() {
        return mField1;
    }

    public void setField1(String field1) {
        this.mField1 = field1;
        notifyPropertyChanged(BR.field1);
    }

    public String getField2() {
        return mField2;
    }

    public void setField2(String field2) {
        this.mField2 = field2;
        notifyPropertyChanged(BR.field2);
    }

    @Bindable({"field1", "field2", "field3", "join"})
    public String getCombo() {
        return mField1 + mJoin + mField2 + mJoin + field3;
    }

    @Bindable({"combo", "join", "field5"} )
    public String getCombo2() {
        return getCombo() + mJoin + getField5();
    }

    public void setJoin(String join) {
        mJoin = join;
        mField4 = join;
        notifyPropertyChanged(BR.join);
    }

    @Bindable
    public String getJoin() {
        return mJoin;
    }

    public String getField4() {
        return mField4 + field3;
    }

    @Bindable
    public String getField5() {
        return mField5;
    }

    @Bindable("val")
    public String getStringVal() {
        return String.valueOf(val.get());
    }

    public void setField5(String val) {
        mField5 = val;
        notifyPropertyChanged(BR.field5);
    }

    // We want to ensure that if dependencies depend on each other recursively that it
    // doesn't cause an infinite loop while building
    @Bindable("recurseDep2")
    public String getRecurseDep() {
        return "hello";
    }

    @Bindable("recurseDep")
    public String getRecurseDep2() {
        return "world";
    }

    @Bindable
    public boolean isThisNameDoesNotMatchAnythingElse1() {
        // see: https://code.google.com/p/android/issues/detail?id=190207
        return false;
    }

    @Bindable
    public boolean getThisNameDoesNotMatchAnythingElse2() {
        return false;
    }

    public String boolMethod(boolean value) {
        return value ? "true" : "false";
    }
}
