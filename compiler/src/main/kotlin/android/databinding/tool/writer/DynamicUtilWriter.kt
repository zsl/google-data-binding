package android.databinding.tool.writer;

class DynamicUtilWriter() {
    public fun write(targetSdk : kotlin.Int) : KCode = kcode("package android.databinding;") {
        nl("")
        nl("import android.os.Build.VERSION;")
        nl("import android.os.Build.VERSION_CODES;")
        nl("")
        nl("public class DynamicUtil {")
        nl("}")
    }
}