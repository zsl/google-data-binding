/*
 * Copyright (C) 2017 The Android Open Source Project
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
package android.databinding.testapp.adapter;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.adapters.ListenerUtil;
import android.databinding.testapp.R;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class CollisionAdapter {
    @BindingAdapter("collisionText")
    public static void setDouble(TextView view, Double val) {
        if (val != null) {
            String currentValue = view.getText().toString();
            if (currentValue.length() != 0) {
                try {
                    double oldVal = Double.parseDouble(currentValue);
                    if (oldVal == val) {
                        return;
                    }
                } catch (NumberFormatException e) {
                    // that's ok, we can just set the value.
                }
            }
            view.setText(val.toString());
        }
    }

    @InverseBindingAdapter(attribute = "collisionText")
    public static Double getDouble(TextView view) {
        try {
            return Double.parseDouble(view.getText().toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @BindingAdapter("collisionText")
    public static void setInteger(TextView view, Integer val) {
        if (val != null) {
            String currentValue = view.getText().toString();
            if (currentValue.length() != 0) {
                try {
                    int oldVal = Integer.parseInt(currentValue);
                    if (oldVal == val) {
                        return;
                    }
                } catch (NumberFormatException e) {
                    // that's ok, we can just set the value.
                }
            }
            view.setText(val.toString());
        }
    }

    @InverseBindingAdapter(attribute = "collisionText")
    public static Integer getInteger(TextView view) {
        try {
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @BindingAdapter("collisionText")
    public static void setLong(TextView view, long val) {
        String currentValue = view.getText().toString();
        if (currentValue.length() != 0) {
            try {
                long oldVal = Long.parseLong(currentValue);
                oldVal /= 10;
                if (oldVal == val) {
                    return;
                }
            } catch (NumberFormatException e) {
                // that's ok, we can just set the value.
            }
        }
        view.setText(String.valueOf(val * 10));
    }

    @InverseBindingAdapter(attribute = "collisionText")
    public static long getLong(TextView view) {
        try {
            return Long.parseLong(view.getText().toString()) / 10;
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    @BindingAdapter("collisionText")
    public static void setLongObj(TextView view, Long val) {
        if (val != null) {
            String currentValue = view.getText().toString();
            if (currentValue.length() != 0) {
                try {
                    long oldVal = Long.parseLong(currentValue);
                    if (oldVal == val) {
                        return;
                    }
                } catch (NumberFormatException e) {
                    // that's ok, we can just set the value.
                }
            }
            view.setText(val.toString());
        }
    }

    @InverseBindingAdapter(attribute = "collisionText")
    public static Long getLongObj(TextView view) {
        try {
            return Long.parseLong(view.getText().toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @BindingAdapter("collisionTextAttrChanged")
    public static void setTextWatcher(TextView view,
            final InverseBindingListener textAttrChanged) {
        final TextWatcher newValue;
        if (textAttrChanged == null) {
            newValue = null;
        } else {
            newValue = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (textAttrChanged != null) {
                        textAttrChanged.onChange();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            };
        }
        final TextWatcher oldValue = ListenerUtil.trackListener(view, newValue, R.id.textWatcher);
        if (oldValue != null) {
            view.removeTextChangedListener(oldValue);
        }
        if (newValue != null) {
            view.addTextChangedListener(newValue);
        }
    }
}
