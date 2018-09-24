package ru.hse.spb

import org.antlr.v4.runtime.ParserRuleContext
import ru.hse.spb.parser.FunBaseVisitor
import ru.hse.spb.parser.FunParser


class InterpretVisitor : FunBaseVisitor<Int>() {

    private val scope = Scope.getBaseScope()

    private var rollBack = false

    override fun visitFile(ctx: FunParser.FileContext): Int {
        return ctx.block().accept(this)
    }

    override fun visitBlock(ctx: FunParser.BlockContext): Int {
        for (statement in ctx.statement()) {
            val result = statement.accept(this)
            if (rollBack) {
                return result
            }
        }
        return 0
    }

    override fun visitBlockWithBraces(ctx: FunParser.BlockWithBracesContext): Int {
        return visitBlock(ctx.block())
    }

    override fun visitStatement(ctx: FunParser.StatementContext): Int {
        return ctx.getChild(0).accept(this)
    }

    override fun visitVariable(ctx: FunParser.VariableContext): Int {
        val name = ctx.identifier().value
        val value = ctx.expression()?.accept(this)
        scope.addNewVariable(name, value)
        return 0
    }

    override fun visitReturnStatement(ctx: FunParser.ReturnStatementContext): Int {
        rollBack = true
        return ctx.expression().accept(this)
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
            else -> throw UnknownOperationException()
        }
    }
}

fun Boolean.toInt() = if (this) 1 else 0

fun Int.toBoolean() = this != 0