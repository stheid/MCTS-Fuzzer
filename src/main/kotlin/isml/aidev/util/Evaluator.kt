package isml.aidev.util

import org.python.core.PyBoolean
import org.python.core.PyDictionary
import org.python.core.PyInteger
import org.python.core.PyString
import org.python.modules._collections.PyDefaultDict
import org.python.util.PythonInterpreter


object Evaluator {
    private val interp = PythonInterpreter().apply {
        exec("from collections import defaultdict")
    }

    private fun Map<String, Int>.toDefaultDict(): PyDefaultDict {
        return PyDefaultDict(
            null,
            this.entries.associate { PyString(it.key) to PyInteger(it.value) }).apply {
            defaultFactory = PyInteger.TYPE
        }
    }

    fun eval(expr: String, vars: Map<String, Int>? = null): Boolean {
        // evaluating expressions
        val code = "res = eval(\"${expr}\",{},loc)"
        interp.set("loc", (vars ?: mapOf()).toDefaultDict())
        interp.exec(code)
        return (interp["res"] as? PyBoolean)?.booleanValue
            ?: error(
                "the result of $code could not be parsed as a boolean. " +
                        "Did you use correct python Syntax (e.g. Boolean literals must be capital like \"True\")"
            )
    }

    fun exec(stmt: String, vars: Map<String, Int>? = null): Map<String, Int> {
        // executing statements
        val code = "exec(\"${stmt}\",{},loc)"
        interp.set("loc", (vars ?: mapOf()).toDefaultDict())
        interp.exec(code)
        val outVars = interp["loc"]
        return (outVars as PyDictionary).toList().associate { it.first as String to it.second as Int }
    }
}