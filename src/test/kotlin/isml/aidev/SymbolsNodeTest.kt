package isml.aidev

import org.junit.jupiter.api.Test/*

internal class SymbolsNodeTest {
    @Test
    fun test1_remainingNTs() {
        var node = SymbolsNode(Symbol.NonTerminal("S").copy())
        listOf(
            RuleEdge(
                listOf(
                    Symbol.NonTerminal("A"),
                    Symbol.Terminal("s"))),
        ).forEach {
            node = node.createChild(it)
        }
        assert(node.remainingNTs.isEmpty())
    }

    @Test
    fun test2_remainingNTs() {
        var node = SymbolsNode(Symbol.NonTerminal("S").copy())
        listOf(
            RuleEdge(
                listOf(
                    Symbol.NonTerminal("A1"),
                    Symbol.NonTerminal("A2"),
                    Symbol.NonTerminal("A3"),
                    Symbol.NonTerminal("A4"),
                    Symbol.Terminal("s"))),
            RuleEdge(
                listOf(
                    Symbol.NonTerminal("A5"),
                    Symbol.Terminal("s"))),
            RuleEdge(
                listOf(
                    Symbol.NonTerminal("A6"),
                    Symbol.Terminal("s"))),
        ).forEach {
            node = node.createChild(it)
        }
        assert(node.remainingNTs.size == 3)
    }
}*/