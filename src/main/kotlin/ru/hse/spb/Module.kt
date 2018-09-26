package ru.hse.spb


class Module {

    private val functions : HashMap<String, DefaultFunction> = HashMap()

    fun contains(name : String): Boolean {
        return functions.containsKey(name)
    }

    fun call(name : String, arguments : List<Int>) : Int {
        return functions[name]?.call(arguments) ?: throw NotDeclaredFunctionException()
    }

    private fun insertFunction(name: String, function: PrintFunction) {
        functions[name] = function
    }

    companion object {
        fun getDefault() :Module {
            val module = Module()
            module.insertFunction("println", PrintFunction())
            return module
        }
    }

}