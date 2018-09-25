package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Test

class InterpreterTest {

    @Test
    fun testEmpty() {
        assert(run("") == 0)
    }

    @Test
    fun testReturn() {
        assert(run("return 1") == 1)
        assert(run("return 0") == 0)
        assert(run("return -2") == -2)
    }

    @Test
    fun testTwoReturns() {
        assertEquals(1, run(
                "return 1\n" +
                "return 2"
        ))
    }

    @Test
    fun testBlock() {
        assertEquals(0, run("var x = 1"))
    }

    @Test
    fun testBlockWithBraces() {
        assertEquals(0, run(
                "if (1) {\n" +
                "   var x = 1\n" +
                "}"
        ))
    }

    @Test
    fun testReturnVariable() {
        assertEquals(1, run(
                "var x = 1\n" +
                "return x"
        ))
    }

    @Test(expected = NotDeclaredVariableException::class)
    fun testNotDeclaredVariable() {
        run("return x")
    }

    @Test(expected = NotInitializedVariableException::class)
    fun testNotInitializedVariable() {
        run("var x\n" +
            "return x")
    }

    @Test(expected = DoubleVariableDeclarationException::class)
    fun testDoubleVariableDeclaration() {
        run("var x\n" +
            "var x")
    }

    @Test(expected = DoubleVariableDeclarationException::class)
    fun testDoubleVariableDeclarationWithInit() {
        run("var x\n" +
            "var x = 1")
    }

    @Test
    fun testBinary() {
        assertEquals(3, run("return 1 + 2"))
        assertEquals(2, run("return 1 * 2"))
        assertEquals(-1, run("return 1 - 2"))
        assertEquals(0, run("return 1 / 2"))
        assertEquals(1, run("return 1 % 2"))
        assertEquals(1, run("return 1 || 2"))
        assertEquals(1, run("return 1 && 2"))
        assertEquals(1, run("return 0 || 2"))
        assertEquals(0, run("return 0 && 2"))
        assertEquals(1, run("return 2 || 0"))
        assertEquals(0, run("return 2 && 0"))
        assertEquals(0, run("return 0 || 0"))
        assertEquals(0, run("return 0 && 0"))

        assertEquals(0, run("return 1 == 2"))
        assertEquals(1, run("return 1 == 1"))
        assertEquals(1, run("return 1 != 2"))
        assertEquals(0, run("return 1 != 1"))

        assertEquals(0, run("return 1 > 2"))
        assertEquals(0, run("return 1 > 1"))
        assertEquals(1, run("return 1 > -1"))
        assertEquals(1, run("return 1 < 2"))
        assertEquals(0, run("return 1 < 1"))
        assertEquals(0, run("return 1 < -1"))

        assertEquals(0, run("return 1 >= 2"))
        assertEquals(1, run("return 1 >= 1"))
        assertEquals(1, run("return 1 >= -1"))
        assertEquals(1, run("return 1 <= 2"))
        assertEquals(1, run("return 1 <= 1"))
        assertEquals(0, run("return 1 <= -1"))
    }

    @Test
    fun testBinaryPriorityMulAdd() {
        assertEquals(7, run("return 1 + 2 * 3"))
        assertEquals(-5, run("return 1 - 2 * 3"))
        assertEquals(4, run("return 4 + 2 / 3"))
        assertEquals(8, run("return 8 - 2 / 3"))
        assertEquals(5, run("return 4 + 4 % 3"))
        assertEquals(7, run("return 8 - 4 % 3"))

        assertEquals(7, run("return 2 * 3 + 1"))
        assertEquals(5, run("return 2 * 3 - 1"))
        assertEquals(4, run("return 2 / 3 + 4"))
        assertEquals(-8, run("return 2 / 3 - 8"))
        assertEquals(5, run("return 4 % 3 + 4"))
        assertEquals(-7, run("return 4 % 3 - 8"))
    }

    @Suppress("SimplifyBooleanWithConstants")
    @Test
    fun testBinaryPriorityAddComp() {
        assertEquals(1, run("return 8 + 2 >= 3"))
        assertEquals(1, run("return 8 - 2 >= 3"))
        assertEquals(0, run("return 8 + 2 <= 3"))
        assertEquals(0, run("return 8 - 2 <= 3"))
        assertEquals(1, run("return 8 + 2 > 3"))
        assertEquals(1, run("return 8 - 2 > 3"))
        assertEquals(0, run("return 8 + 2 < 3"))
        assertEquals(0, run("return 8 - 2 < 3"))

        assertEquals(0, run("return 2 >= 3 + 8"))
        assertEquals(1, run("return 2 >= 3 - 8"))
        assertEquals(1, run("return 2 <= 3 + 8"))
        assertEquals(0, run("return 2 <= 3 - 8"))
        assertEquals(0, run("return 2 >= 3 + 8"))
        assertEquals(1, run("return 2 >= 3 - 8"))
        assertEquals(1, run("return 2 <= 3 + 8"))
        assertEquals(0, run("return 2 <= 3 - 8"))
    }

    @Test
    fun testBinaryPriorityCompEq() {
        assertEquals(0, run("return 10 > 0 == 11"))
        assertEquals(0, run("return 12 > 11 != 1"))
        assertEquals(0, run("return 11 == 0 < 10"))
        assertEquals(0, run("return 1 != 11 < 12"))
    }

    @Test
    fun testBinaryPriorityEqLogical() {
        assertEquals(0, run("return 0 == 11 && 0"))
        assertEquals(0, run("return 1 == 11 || 0"))
        assertEquals(0, run("return 0 && 11 == 0"))
        assertEquals(0, run("return 0 && 11 == 1"))
    }

    @Test
    fun testBinaryPriorityBrackets() {
        assertEquals(7, run("return 1 + (2 * 3)"))
        assertEquals(9, run("return (1 + 2) * 3"))
    }

    @Test
    fun testBinaryChain() {
        assertEquals(6, run("return 1 + 2 + 3"))
        assertEquals(24, run("return 2 * 3 * 4"))
        assertEquals(2, run("return 2 % 3 % 4"))
        assertEquals(0, run("return 2 > 3 > 4"))
        assertEquals(1, run("return 2 < 3 < 4"))
        assertEquals(1, run("return 2 == 3 == 0"))
        assertEquals(1, run("return 2 || 3 || 0"))
    }

    @Test
    fun testAssignment() {
        assertEquals(2, run(
                "var x = 1\n" +
                "x = 2\n" +
                "return x"
        ))
        assertEquals(2, run(
                "var x\n" +
                "x = 2\n" +
                "return x"
        ))
    }

    @Test
    fun testIfTrue() {
        assertEquals(2, run(
                "var x = 1\n" +
                "if (7) {\n" +
                "   x = 2\n" +
                "} else {\n" +
                "   x = 3\n" +
                "}\n" +
                "return x"
        ))
    }

    @Test
    fun testIfTrueNoElse() {
        assertEquals(2, run(
                "var x = 1\n" +
                "if (56) {\n" +
                "   x = 2\n" +
                "}\n" +
                "return x"
        ))
    }

    @Test
    fun testIfFalse() {
        assertEquals(3, run(
                "var x = 1\n" +
                "if (0) {\n" +
                "   x = 2\n" +
                "} else {\n" +
                "   x = 3\n" +
                "}\n" +
                "return x"
        ))
    }


    @Test
    fun testIfFalseNoElse() {
        assertEquals(1, run(
                "var x = 1\n" +
                "if (0) {\n" +
                "   x = 2\n" +
                "}\n" +
                "return x"
        ))
    }

    @Test
    fun testIfReturn() {
        assertEquals(2, run(
                "if (1) {\n" +
                "   return 2\n" +
                "   return 3\n" +
                "}\n" +
                "return 1"
        ))
    }

    @Test
    fun testIfReturnWithElse() {
        assertEquals(3, run(
                "if (0) {\n" +
                "   return 2\n" +
                "   return 4\n" +
                "} else {\n" +
                "   return 3\n" +
                "   return 5\n" +
                "}\n" +
                "return 1"
        ))
    }

    @Test
    fun testWhileFalse() {
        assertEquals(2, run(
                "while (0) {\n" +
                "   return 1\n" +
                "}\n" +
                "return 2"
        ))
    }

    @Test
    fun testWhileTrue() {
        assertEquals(1, run(
                "while (1) {\n" +
                "   return 1\n" +
                "}\n" +
                "return 2"
        ))
    }

    @Test
    fun testWhileWithState() {
        assertEquals(4, run(
                "var x = 1\n" +
                "while (x != 4) {\n" +
                "   x = x + 1\n" +
                "}\n" +
                "return x"
        ))
    }

    @Test
    fun testWhileWithInnerIf() {
        assertEquals(7, run(
                "var x = 1\n" +
                "while (x != 4) {\n" +
                "   x = x + 1\n" +
                "   if (x == 3) {\n" +
                "       return 7\n" +
                "   }\n" +
                "}\n" +
                "return x"
        ))
    }

    @Test
    fun testFunctionCallNoArgs() {
        assertEquals(2, run(
                "fun foo() {\n" +
                 "  return 2\n" +
                 "}\n" +
                "return foo()\n"
        ))
        assertEquals(2, run(
                "fun foo() {\n" +
                "   return 2\n" +
                "   return 3\n" +
                "}\n" +
                "return foo()\n"
        ))
        assertEquals(24, run(
                "var n = 4\n" +
                "var x = 1\n" +
                "fun fact() {\n" +
                "   if (n != 0) {\n" +
                "       x = x * n\n" +
                "       n = n - 1\n" +
                "       return fact()\n" +
                "   } else {\n" +
                "       return x\n" +
                "   }\n" +
                "}\n" +
                "return fact()"
        ))
    }

    @Test
    fun testFunctionCallArgument() {
        assertEquals(2, run(
                "fun id(x) {\n" +
                "   return x\n" +
                "}\n" +
                "return id(2)"
        ))
    }

    @Test
    fun testFunctionCallTwoArguments() {
        assertEquals(3, run(
                "fun sum(a, b) {\n" +
                "   return a + b\n" +
                "}\n" +
                "return sum(1, 2)"
        ))
    }

    @Test
    fun testFunctionCallVariable() {
        assertEquals(3, run(
                "var x = 3" +
                "fun id(a) {" +
                "   return a" +
                "}" +
                "return id(x)"
        ))
    }

    @Test
    fun testFunctionCallVariableShadowing() {
        assertEquals(3, run(
                "var x = 1\n" +
                "fun id(x) {\n" +
                "   return x + 1\n" +
                "}\n" +
                "return id(2)\n"
        ))
    }

    @Test
    fun testRecursive() {
        assertEquals(24, run(
                "fun fact(x) {\n" +
                "   if (x == 0) {\n" +
                "       return 1\n" +
                "   } else {\n" +
                "       return fact(x - 1) * x\n" +
                "   }\n" +
                "}\n" +
                "return fact(4)\n"
        ))
    }

    @Test
    fun testInnerBlockShadowing() {
        assertEquals(3, run("" +
                "var x = 3\n" +
                "var y = 0\n" +
                "while (y < 2) {\n" +
                "   y = y + 1\n" +
                "   var x = 4\n" +
                "   x = 0\n" +
                "}\n" +
                "return x"
        ))
    }

    @Test
    fun testInnerBlockFunction() {
        assertEquals(5, run("" +
                "fun foo(x) {\n" +
                "   return x + 1\n" +
                "}\n" +
                "\n" +
                "var x = 3\n" +
                "if (x == 3) {\n" +
                "   fun foo(x) {\n" +
                "       return x + 2\n" +
                "   }\n" +
                "   x = foo(x)\n" +
                "}\n" +
                "\n" +
                "return x"
        ))
    }

    @Test
    fun testPassValue() {
        assertEquals(2, run("" +
                "fun inc(x) {" +
                "   x = x + 1" +
                "}" +
                "var x = 2" +
                "inc(x)" +
                "return x"
        ))
    }

    @Test
    fun testFunctionSideEffect() {
        assertEquals(2, run("" +
                "var cnt = 0" +
                "fun inc() {" +
                "   cnt = cnt + 1" +
                "}" +
                "" +
                "inc()" +
                "inc()" +
                "return cnt"
        ))
    }

    @Test
    fun testThirdDefault() {
        assertEquals(42, run("" +
                "fun foo(n) {\n" +
                "    fun bar(m) {\n" +
                "        return m + n\n" +
                "    }\n" +
                "\n" +
                "    return bar(1)\n" +
                "}\n" +
                "\n" +
                "return foo(41)"
        ))
    }

    @Test(expected = WrongAmountOfArguments::class)
    fun testWrongAmountOfArgumentsMore() {
        run(
                "fun foo() {\n" +
                "   return 1\n" +
                "}\n" +
                "foo(1)"
        )
    }

    @Test(expected = WrongAmountOfArguments::class)
    fun testWrongAmountOfArgumentsMore2() {
        run(
                "fun foo(x) {\n" +
                "   return 1\n" +
                "}\n" +
                "foo(1, 2)"
        )
    }

    @Test(expected = WrongAmountOfArguments::class)
    fun testWrongAmountOfArgumentsLess() {
        run(
                "fun foo(x, y) {\n" +
                "   return 1\n" +
                "}\n" +
                "foo(2)"
        )
    }

    @Test(expected = WrongAmountOfArguments::class)
    fun testWrongAmountOfArgumentsLess2() {
        run(
                "fun foo(x) {\n" +
                "   return 1\n" +
                "}\n" +
                "foo()"
        )
    }

    @Test(expected = DoubleFunctionDeclarationException::class)
    fun testDoubleFunctionDeclaration() {
        run(
            "fun foo() {}\n" +
            "fun foo(x) {}"
        )
    }

    @Test(expected = NotDeclaredFunctionException::class)
    fun testNotDeclaredFunctionException() {
        run(
                "return foo()"
        )
    }

}