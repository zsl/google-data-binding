package android.databinding.tool.writer;

import android.databinding.tool.expr.ExprModel
import android.databinding.tool.reflection.ModelAnalyzer
import android.databinding.tool.reflection.ModelClass

@Suppress("UNUSED_PARAMETER") // TODO remove in a followup CL, this class is not used anymore.
class DynamicUtilWriter {
    fun write(targetSdk : kotlin.Int) : KCode = kcode("package android.databinding;") {
        nl("")
        nl("import android.os.Build.VERSION;")
        nl("import android.os.Build.VERSION_CODES;")
        nl("import android.databinding.BindingConversion;")
        nl("")
        annotateWithGenerated()
        block("public class DynamicUtil") {
            val analyzer = ModelAnalyzer.getInstance();
            ModelClass.UNBOX_MAPPING.forEach {
                block("public static ${it.value.simpleName} ${ExprModel.SAFE_UNBOX_METHOD_NAME}(${it.key.canonicalName} boxed)") {
                    nl("return boxed == null ? ${analyzer.getDefaultValue(it.value.simpleName)} : (${it.value.canonicalName})boxed;");
                }
            }
        }
   }
}
