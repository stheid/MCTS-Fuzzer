package isml.aidev.util

import isml.aidev.RuleEdge
import isml.aidev.SymbolsNode
import isml.aidev.grammar.Symbol
import org.api4.java.datastructure.graph.ILabeledPath

fun ILabeledPath<SymbolsNode, RuleEdge>.toWord(): String {
    val node = this.root
    val symbol = node.currNT!!
    val symbols = Chain(listOf<Symbol>(symbol))
    val nts = hashMapOf(symbol to symbols.linkIterator().asSequence().first())

    // root has been processed, now we look at the production rules and the successor nodes
    arcs.zip(nodes.zipWithNext()).forEach { (rule, nodepair) ->
        // dereference chainlink (GC) and prepare for substitution
        val linkToSubstitute = nts.remove(nodepair.first.currNT!!)!!

        if (rule.substitution.isEmpty()) {
            // for ε-rules just remove the reference
            linkToSubstitute.substitute(null)
        } else {
            // for non-ε rules:
            // create the substitution by melding the unique non-terminals from the node and adding the missing
            // terminals from the rule
            // e.g.  substitutionNTs: B3,D4,D6 (unique NTs); rule.substitution: aBcDDc (nts and terminals)
            val sub = nodepair.second.substitutionNTs.iterator().let { iterator ->
                rule.substitution.map {
                    // if it's a non-terminal, take the equivalent Unique<NonTerminal> from the node
                    it as? Symbol.Terminal ?: iterator.next()
                }
            }
            val substChain = Chain(sub)

            // store references to chainlinks containing non-terminals
            substChain.linkIterator().asSequence()
                .forEach {
                    if (it.value is Symbol.NonTerminal)
                        nts[it.value] = it
                }

            // substitute chainlink with chain
            linkToSubstitute.substitute(substChain)
        }
    }

    return symbols.filterIsInstance(Symbol.Terminal::class.java).joinToString(separator = "") { it.value }
}
