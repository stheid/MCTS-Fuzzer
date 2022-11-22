package isml.aidev.starlib

import isml.aidev.RuleEdge
import isml.aidev.SymbolsNode
import isml.aidev.grammar.Grammar
import org.api4.java.datastructure.graph.implicit.IGraphGenerator
import org.api4.java.datastructure.graph.implicit.ISingleRootGenerator

class PCFGGraphGenerator(private val grammar: Grammar) : IGraphGenerator<SymbolsNode, RuleEdge> {
    override fun getRootGenerator() =
        // during root generation we can create state that exists for the time of one evaluation
        ISingleRootGenerator { SymbolsNode(grammar.startSymbol) }

    override fun getSuccessorGenerator() =
        PCFGSuccGen(grammar)
}