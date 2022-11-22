package isml.aidev.grammar

import isml.aidev.util.Chain
import org.junit.jupiter.api.Test

class GrammarGraphTest {
    @Test
    fun nodeTest() {
        val grammar = Grammar.fromResource("extremely_simple_gram.yml")
        val node1 = Node(Chain(listOf(grammar.startSymbol)))
        println(node1.toString())
    }

    @Test
    fun toGraphTest() {
        val grammar = Grammar.fromResource("extremely_simple_gram.yml", false)
        val graph = grammar.prodRules.toGraph()
        assert(graph.vertexSet().toList().size == 10)
    }
}