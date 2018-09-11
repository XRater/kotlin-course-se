package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Test

class TestSource {
    @Test
    fun testFirstFromCodeforces() {
        val queries = listOf("2", "a", "1", "b", "2", "c")
        runSimpleTest(2, "bac", queries, "acb")
    }

    @Test
    fun testSecondFromCodeforces() {
        val queries = listOf("1", "a", "1", "a", "1", "c", "2", "b")
        runSimpleTest(1, "abacaba", queries, "baa")
    }

    private fun runSimpleTest(k : Int, s : String, queries : List<String>, answer : String) {
        val changes = ArrayList<Change>()
        var index = 0
        for ((i, arg) in queries.withIndex()) {
            if (i % 2 == 0) {
                index = arg.toInt()
            } else {
                changes.add(Change(arg[0], index))
            }
        }

        val solver = Solver(k, s, changes)
        solver.solve()
        assertEquals(solver.getSolution(), answer)
    }
}