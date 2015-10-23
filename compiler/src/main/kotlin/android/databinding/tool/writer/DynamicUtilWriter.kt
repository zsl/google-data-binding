package android.databinding.tool.writer


class DynamicUtilWriter() {
    public fun write() : String {
        return """
package android.databinding;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;

public class DynamicUtil {
    public static ColorStateList getColorStateListFromResource(View root, int resourceId) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            return root.getContext().getColorStateList(resourceId);
        } else {
            return root.getResources().getColorStateList(resourceId);
        }
    }

    public static Drawable getDrawableFromResource(View root, int resourceId) {
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            return root.getContext().getDrawable(resourceId);
        } else {
            return root.getResources().getDrawable(resourceId);
        }
    }
}
"""
    }
}