package isml.aidev.starlib

import isml.aidev.RuleEdge
import isml.aidev.SymbolsNode
import isml.aidev.grammar.Grammar
import org.api4.java.ai.graphsearch.problem.IPathSearchInput
import org.api4.java.ai.graphsearch.problem.implicit.graphgenerator.IPathGoalTester


class PCFGSearchInput(private val grammar: Grammar) : IPathSearchInput<SymbolsNode, RuleEdge> {
    override fun getGraphGenerator() =
        PCFGGraphGenerator(grammar)

    override fun getGoalTester() =
        IPathGoalTester<SymbolsNode, RuleEdge> { it.head.isFinished }
}