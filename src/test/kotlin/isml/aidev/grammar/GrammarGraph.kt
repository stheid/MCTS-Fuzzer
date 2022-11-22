package isml.aidev.grammar

import org.jgrapht.nio.Attribute
import org.jgrapht.nio.DefaultAttribute
import org.jgrapht.nio.dot.DOTExporter
import java.io.File

fun createExporter(): DOTExporter<Node, Edge> {
    val exporter = DOTExporter<Node, Edge> {
        when (it) {
            is ConditionalNode -> """"${it.cond + " " + it.hashCode().toString().substring(0..4)}""""

            else -> """"${
                it.nodes.toString().filter { it.isLetterOrDigit() || it == ' ' } + " " + it.hashCode().toString()
                    .substring(0..4)
            } ${it.expectedExpansions}""""
        }
    }

    exporter.setVertexAttributeProvider {
        mutableMapOf<String, Attribute>(
            "color" to DefaultAttribute.createAttribute(
                when (it) {
                    is ComplexNode -> "red"
                    is ConditionalNode -> "blue"
                    else -> "black"
                }
            )
        )
    }
    exporter.setEdgeAttributeProvider {
        mutableMapOf<String, Attribute>(
            "color" to DefaultAttribute.createAttribute(
                when (it) {
                    is ComplexEdge -> "red"
                    is CondEdge -> "blue"
                    else -> "black"
                }
            ), "label" to DefaultAttribute.createAttribute(
                when (it) {
                    is RuleEdgeSimplify -> listOfNotNull(it.statement, "%.2f".format(it.weight)).joinToString()
                    else -> ""
                }
            )
        )
    }
    return exporter
}

fun main() {
    //val grammar = Grammar.fromResource("simple_annotated_globvar.yaml", false)
    //val grammar = Grammar.fromResource("simplify_4.yaml", false)
    //val grammar = Grammar.fromResource("simple_annotated_grammargraph.yaml", false)
//  val grammar = Grammar.fromResource("extremely_simple_gram.yml")
//    val grammar = Grammar.fromResource("js_gen.yml")
    //val grammar = Grammar.fromResource("xml_gen_annot.yaml")
    //val grammar = Grammar.fromResource("complex_anot.yaml", false)
    val grammar = Grammar.fromResource("test_expectation1.yaml", false)

    println(grammar)
    var graph = grammar.prodRules.toGraph()

    createExporter().exportGraph(graph, File("grammar_raw.dot").bufferedWriter())
    graph = graph.simplify().calculateExpectation(grammar.startSymbol)
    createExporter().exportGraph(graph, File("grammar_simple.dot").bufferedWriter())

    val rules = graph.toProdRules(grammar.startSymbol)
    println(rules)
}
