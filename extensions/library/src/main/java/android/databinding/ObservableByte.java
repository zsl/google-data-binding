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
package android.databinding;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * An observable class that holds a primitive byte.
 * <p>
 * Observable field classes may be used instead of creating an Observable object. It can also
 * create a calculated field, depending on other fields:
 * <pre><code>public class MyDataObject {
 *     public final ObservableByte flags = new ObservableByte();
 *     public final ObservableByte otherFlags = new ObservableByte();
 *     public final ObservableByte mergedFlags = new ObservableByte(flags, otherFlags) {
 *         &#64;Override
 *         public byte get() { return flags.get() | otherFlags.get(); }
 *     };
 * }</code></pre>
 * Fields of this type should be declared final because bindings only detect changes in the
 * field's value, not of the field itself.
 * <p>
 * This class is parcelable and serializable but callbacks are ignored when the object is
 * parcelled / serialized. Unless you add custom callbacks, this will not be an issue because
 * data binding framework always re-registers callbacks when the view is bound. A parceled
 * ObservableByte will lose its dependencies.
 */
public class ObservableByte extends BaseObservableField implements Parcelable, Serializable {
    static final long serialVersionUID = 1L;
    private byte mValue;

    /**
     * Creates an ObservableByte with the given initial value.
     *
     * @param value the initial value for the ObservableByte
     */
    public ObservableByte(byte value) {
        mValue = value;
    }

    /**
     * Creates an ObservableByte with the initial value of <code>0</code>.
     */
    public ObservableByte() {
    }

    /**
     * Creates an ObservableByte that depends on {@code dependencies}. Typically,
     * {@link ObservableField}s are passed as dependencies. When any dependency
     * notifies changes, this ObservableByte also notifies a change.
     *
     * @param dependencies The Observables that this ObservableByte depends on.
     */
    public ObservableByte(Observable... dependencies) {
        super(dependencies);
    }

    /**
     * @return the stored value.
     */
    public byte get() {
        return mValue;
    }

    /**
     * Set the stored value.
     *
     * @param value The new value
     */
    public void set(byte value) {
        if (value != mValue) {
            mValue = value;
            notifyChange();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(mValue);
    }

    public static final Parcelable.Creator<ObservableByte> CREATOR
            = new Parcelable.Creator<ObservableByte>() {

        @Override
        public ObservableByte createFromParcel(Parcel source) {
            return new ObservableByte(source.readByte());
        }

        @Override
        public ObservableByte[] newArray(int size) {
            return new ObservableByte[size];
        }
    };
}
