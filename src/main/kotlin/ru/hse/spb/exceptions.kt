package ru.hse.spb

import java.lang.Exception


abstract class InterpretationException : Exception() {

    abstract fun message() : String

}

class NotDeclaredVariableException(private val name : String) : InterpretationException() {

    override fun message(): String {
        return "Not declared variable $name"
    }

}

class NotInitializedVariableException(private val name : String) : InterpretationException() {

    override fun message(): String {
        return "Variable $name is not initialized"
    }

}

class DoubleVariableDeclarationException(private val name : String) : InterpretationException() {

    override fun message(): String {
        return "Double declaration of variable $name"
    }

}

class DoubleFunctionDeclarationException(private val name : String) : InterpretationException() {

    override fun message(): String {
        return "Double declaration of function $name"
    }

}

class WrongAmountOfArguments(
        private val name : String,
        private val required : Int,
        private val passed : Int
) : InterpretationException() {

    override fun message(): String {
        return "Wrong amount of arguments for function $name. " +
                "Required $required but passed $passed"
    }

}

class NotDeclaredFunctionException(private val name : String) : InterpretationException() {

    override fun message(): String {
        return "Not declared function $name"
    }

}

