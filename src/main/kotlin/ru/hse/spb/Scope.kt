package ru.hse.spb

import ru.hse.spb.parser.FunParser


class Scope(val parentScope : Scope?) {

    private val variables : MutableMap<String, Int?> = HashMap()
    private val functions : MutableMap<String, FunParser.FunctionContext> = HashMap()

    fun getValue(name : String) : Int {
        return if (variables.containsKey(name)) {
            variables[name] ?: throw NotInitializedVariableException(name)
        } else {
            parentScope?.getValue(name) ?: throw NotDeclaredVariableException(name)
        }
    }

    fun setVariable(name : String, value : Int) {
        if (!variables.containsKey(name)) {
            parentScope?.setVariable(name, value) ?: throw NotDeclaredVariableException(name)
            return
        }
        variables[name] = value
    }

    fun addNewVariable(name: String, value: Int?) {
        if (variables.containsKey(name)) {
            throw DoubleVariableDeclarationException(name)
        }
        variables[name] = value
    }

    fun getFunction(name : String) : FunParser.FunctionContext {
        return if (functions.containsKey(name)) {
            return functions[name]!!
        } else {
            parentScope?.getFunction(name) ?: throw NotDeclaredFunctionException(name)
        }
    }

    fun addNewFunction(name : String, function : FunParser.FunctionContext) {
        if (functions.containsKey(name)) {
            throw DoubleFunctionDeclarationException(name)
        }
        functions[name] = function
    }

    fun childScope(): Scope {
        return Scope(this)
    }

    companion object {

        fun getBaseScope() : Scope {
            return Scope(null)
        }
    }
}