package ru.hse.spb

import org.junit.Test
import ru.hse.spb.BaseTest.Companion.getFileFromTextExpectingError


class ParserTestErrors {

    @Test
    fun testIncompele() {
        getFileFromTextExpectingError("()")
        getFileFromTextExpectingError("{}")
        getFileFromTextExpectingError("{var a}")
    }

    @Test
    fun testVariableIncomplete() {
        getFileFromTextExpectingError("var")
    }

    @Test
    fun testVariablebadNames() {
        getFileFromTextExpectingError("var 1b")
        getFileFromTextExpectingError("var %a")
        getFileFromTextExpectingError("var a%")
    }

    /* Binary expression */

    @Test
    fun testBinaryExpression() {
        getFileFromTextExpectingError(")1( + 2")
        getFileFromTextExpectingError("(1 + 2")
        getFileFromTextExpectingError("()1 + 2")
        getFileFromTextExpectingError("[1 + 2]")
    }

    /* Assignment */

    @Test
    fun testAssignment() {
        getFileFromTextExpectingError("(a = 2)")
        getFileFromTextExpectingError("(a = b)")
        getFileFromTextExpectingError("a = b = c")
        getFileFromTextExpectingError("a = b = 1")
        getFileFromTextExpectingError("a = 1 = c")
        getFileFromTextExpectingError("1 = a")
        getFileFromTextExpectingError("1 = 1")
        getFileFromTextExpectingError("(1 = 1)")
        getFileFromTextExpectingError("{a = 1}")
    }

    @Test
    fun testVariableAssignment() {
        getFileFromTextExpectingError("(var x = 1)")
        getFileFromTextExpectingError("{var x = 1}")
        getFileFromTextExpectingError("var x = {1}")
        getFileFromTextExpectingError("var x = 1 = 1")
    }

    /* Function call */

    @Test
    fun testFunctionCall() {
        getFileFromTextExpectingError("foo(var a)")
        getFileFromTextExpectingError("foo(())")
        getFileFromTextExpectingError("foo(a = 1)")
        getFileFromTextExpectingError("foo({})")
    }

    /* Test function */

    @Test
    fun testFunction() {
        getFileFromTextExpectingError("fun foo()")
        getFileFromTextExpectingError("fun foo(1)")
        getFileFromTextExpectingError("fun foo(1 + 2) {}")
    }
}