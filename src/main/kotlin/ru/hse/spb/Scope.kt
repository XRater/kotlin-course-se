package ru.hse.spb


class Scope(private val parentScope : Scope?) {

    private val variables : MutableMap<String, Int?> = HashMap()
    private val functions : MutableMap<String, Int> = HashMap()

    public fun getValue(name : String) : Int {
        return if (variables.containsKey(name)) {
            variables[name] ?: throw NotInitializedVariableException()
        } else {
            parentScope?.getValue(name) ?: throw NotDeclaredVariableException()
        }
    }

    public fun setVariable(name : String, value : Int) {
        variables[name] = value
    }

    public fun addNewVariable(name: String) {
        addNewVariable(name, null)
    }

    public fun addNewVariable(name: String, value: Int?) {
        if (variables.containsKey(name)) {
            throw DoubleVariableDeclarationException()
        }
        variables[name] = value
    }

    public fun callFunction(name : String) : Int {
        TODO()
    }

    companion object {

        fun getBaseScope() : Scope {
            return Scope(null)
        }
    }
}