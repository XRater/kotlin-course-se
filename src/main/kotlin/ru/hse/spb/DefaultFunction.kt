package ru.hse.spb


interface DefaultFunction {

    fun call(arguments : List<Int>) : Int?

}

class PrintFunction : DefaultFunction {

    override fun call(arguments: List<Int>) : Int {
        print(arguments.joinToString(separator = " ", postfix = "\n"))
        return 0
    }

}
