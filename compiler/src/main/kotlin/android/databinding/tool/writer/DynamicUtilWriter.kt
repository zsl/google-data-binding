package android.databinding.tool.writer;

class DynamicUtilWriter() {
    public fun write(targetSdk : kotlin.Int) : KCode = kcode("package android.databinding;") {
        nl("")
        nl("import android.content.res.ColorStateList;")
        nl("import android.graphics.drawable.Drawable;")
        nl("import android.os.Build.VERSION;")
        nl("import android.os.Build.VERSION_CODES;")
        nl("import android.view.View;")
        nl("")
        nl("public class DynamicUtil {")
        tab("@SuppressWarnings(\"deprecation\")") {
            nl("public static ColorStateList getColorStateListFromResource(View root, int resourceId) {")
            if (targetSdk >= 23) {
                tab("if (VERSION.SDK_INT >= VERSION_CODES.M) {") {
                    tab("return root.getContext().getColorStateList(resourceId);")
                }
                tab("}")
            }
            tab("return root.getResources().getColorStateList(resourceId);")
        }
        tab("}")
        nl("")

        tab("@SuppressWarnings(\"deprecation\")") {
            nl("public static Drawable getDrawableFromResource(View root, int resourceId) {")
            if (targetSdk >= 21) {
                tab("if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {") {
                    tab("return root.getContext().getDrawable(resourceId);")
                }
                tab("}")
            }
            tab("return root.getResources().getDrawable(resourceId);")
        }
        tab("}")
        nl("}")
    }
}