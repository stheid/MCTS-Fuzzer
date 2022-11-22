package isml.aidev.starlib

import isml.aidev.RuleEdge
import isml.aidev.SymbolsNode
import isml.aidev.grammar.Grammar
import org.api4.java.datastructure.graph.implicit.INewNodeDescription
import org.api4.java.datastructure.graph.implicit.ISuccessorGenerator

class PCFGSuccGen(private val grammar: Grammar) : ISuccessorGenerator<SymbolsNode, RuleEdge> {
    //private val cache = HashMap<SymbolsNode, List<INewNodeDescription<SymbolsNode, RuleEdge>>>()

    override fun generateSuccessors(node: SymbolsNode): List<INewNodeDescription<SymbolsNode, RuleEdge>> {
        //cache.computeIfAbsent(node) {
        // else create new children and filter for existing ones
        return node.currNT?.run {
            grammar.validRules(this, node.vars(this))
                .map { node.createChild(it) to it }
                .map { (child, rule) ->
                    object : INewNodeDescription<SymbolsNode, RuleEdge> {
                        override fun getFrom() = node
                        override fun getTo() = child
                        override fun getArcLabel() = rule
                    }
                }
        } ?: emptyList()
        //}
    }
}