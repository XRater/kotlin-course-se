package ru.hse.spb

import org.antlr.v4.runtime.ParserRuleContext
import ru.hse.spb.parser.FunBaseVisitor
import ru.hse.spb.parser.FunParser

class InterpretVisitor : FunBaseVisitor<Int>() {

    private var scope = Scope.getBaseScope()

    private var rollBack = false
    private var returnValue : Int? = null

    override fun visitFile(ctx: FunParser.FileContext): Int {
        ctx.block().accept(this)
        return returnValue ?: 0
    }

    override fun visitBlock(ctx: FunParser.BlockContext): Int? {
        for (statement in ctx.statement()) {
            statement.accept(this)
            if (rollBack) {
                return null
            }
        }
        return null
    }

    override fun visitBlockWithBraces(ctx: FunParser.BlockWithBracesContext): Int? {
        scope = scope.childScope()
        ctx.block().accept(this)
        scope = scope.parentScope!!
        return null
    }

    override fun visitStatement(ctx: FunParser.StatementContext): Int? {
        return ctx.getChild(0).accept(this)
    }

    override fun visitFunction(ctx: FunParser.FunctionContext): Int? {
        scope.addNewFunction(ctx.identifier().value, ctx)
        return null
    }

    override fun visitVariable(ctx: FunParser.VariableContext): Int? {
        val name = ctx.identifier().value
        val value = ctx.expression()?.accept(this)
        scope.addNewVariable(name, value)
        return null
    }

    override fun visitIfStatement(ctx: FunParser.IfStatementContext): Int? {
        val condition = ctx.expression().accept(this).toBoolean()
        val ifBranch = ctx.blockWithBraces(0)
        val elseBranch = ctx.blockWithBraces(1)
        if (condition) {
            ifBranch.accept(this)
        } else {
            elseBranch?.accept(this)
        }
        return null
    }

    override fun visitWhileStatement(ctx: FunParser.WhileStatementContext): Int? {
        while (!rollBack && ctx.expression().accept(this).toBoolean()) {
            ctx.blockWithBraces().accept(this)
        }
        return null
    }

    override fun visitAssignment(ctx: FunParser.AssignmentContext): Int? {
        scope.setVariable(ctx.identifier().text, ctx.expression().accept(this))
        return null
    }

    override fun visitReturnStatement(ctx: FunParser.ReturnStatementContext): Int? {
        returnValue = ctx.expression().accept(this)
        rollBack = true
        return null
    }

    override fun visitFunctionCall(ctx: FunParser.FunctionCallContext): Int {
        val function = scope.getFunction(ctx.identifier().value)
        val arguments = ctx.arguments().expression().map { expr -> expr.accept(this) }
        val argumentsNames = function.parameterNames().identifier().map { x -> x.value }
        val argumentsRequired = function.parameterNames().identifier().size
        if (arguments.size != argumentsRequired) {
            throw WrongAmountOfArguments()
        }

        scope = scope.childScope()
        for (i in 0 until argumentsRequired) {
            scope.addNewVariable(argumentsNames[i], arguments[i])
        }
        function.blockWithBraces().accept(this)

        scope = scope.parentScope!!
        val result = returnValue ?: 0
        returnValue = null
        rollBack = false
        return result
    }

    override fun visitExpression(ctx: FunParser.ExpressionContext): Int {
        return ctx.getChild(0).accept(this)
    }

    override fun visitLogical(ctx: FunParser.LogicalContext): Int {
        return visitBinary(ctx)
    }

    override fun visitEquality(ctx: FunParser.EqualityContext): Int {
        return visitBinary(ctx)
    }

    override fun visitComparison(ctx: FunParser.ComparisonContext): Int {
        return visitBinary(ctx)
    }

    override fun visitAddition(ctx: FunParser.AdditionContext): Int {
        return visitBinary(ctx)
    }

    override fun visitMultiplication(ctx: FunParser.MultiplicationContext): Int {
        return visitBinary(ctx)
    }

    override fun visitAtomic(ctx: FunParser.AtomicContext): Int {
        return ctx.expression()?.accept(this) ?: ctx.getChild(0).accept(this)
    }

    override fun visitIdentifier(ctx: FunParser.IdentifierContext): Int {
        return scope.getValue(ctx.value)
    }

    override fun visitLiteral(ctx: FunParser.LiteralContext): Int {
        return ctx.value
    }

    private fun visitBinary(ctx: ParserRuleContext) : Int {
        if (ctx !is FunParser.LogicalContext &&
                ctx !is FunParser.EqualityContext &&
                ctx !is FunParser.ComparisonContext &&
                ctx !is FunParser.AdditionContext &&
                ctx !is FunParser.MultiplicationContext) {
            throw RuntimeException() // illegal code usage
        }
        var result = ctx.getChild(0).accept(this)
        var op : String? = null
        for ((index, child) in ctx.children.withIndex()) {
            when {
                index == 0 -> {}
                index % 2 == 1 -> op = child.text
                else -> result = applyOperation(result, op, child.accept(this))
            }
        }
        return result
    }

    private fun applyOperation(first : Int, op : String?, second : Int) : Int {
        return when(op) {
            "+" -> first + second
            "-" -> first - second
            "*" -> first * second
            "/" -> first / second
            "%" -> first % second
            ">" -> (first > second).toInt()
            "<" -> (first < second).toInt()
            ">=" -> (first >= second).toInt()
            "<=" -> (first <= second).toInt()
            "==" -> (first == second).toInt()
            "!=" -> (first != second).toInt()
            "&&" -> (first.toBoolean() && second.toBoolean()).toInt()
            "||" -> (first.toBoolean() || second.toBoolean()).toInt()
            else -> throw RuntimeException() //error while parsing
        }
    }
}

fun Boolean.toInt() = if (this) 1 else 0

fun Int.toBoolean() = this != 0