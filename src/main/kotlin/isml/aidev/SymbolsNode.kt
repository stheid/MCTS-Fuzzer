package isml.aidev

import isml.aidev.grammar.Symbol
import isml.aidev.util.Evaluator

/**
 * Must not be a data-class because we use object references for equality and caching
 */
class SymbolsNode(
    val currNT: Symbol.NonTerminal?,
    val substitutionNTs: List<Symbol.NonTerminal> = emptyList(),
    private val parent: SymbolsNode? = null,
    private val globalvars: Map<String, Int> = mapOf(),
    private val localvars: Map<Symbol.NonTerminal, Map<String, Int>> = mapOf(),
    val depth: Int = 0,
) {
    val isFinished
        get() = currNT == null

    val remainingNTs: Set<Symbol.NonTerminal> by lazy {
        // recursively calculate the remaining non-terminals from the parents
        // get the parents nts or create an empty set
        (parent?.remainingNTs?.toMutableSet() ?: mutableSetOf())
            .apply {
                addAll(substitutionNTs)
                remove(currNT)
            }
    }

    fun vars(nt: Symbol.NonTerminal): Map<String, Int> =
        globalvars + (localvars.get(nt) ?: mapOf())

    override fun toString() =
        "SymbolNode(currNT=${currNT}, remainingNTs=$remainingNTs, depth=${depth}, globalvars=$globalvars, localvars=$localvars)"

    fun createChild(rule: RuleEdge): SymbolsNode {
        val newVars = rule.expression?.let {
            Evaluator.exec(it, vars(currNT!!))
        }

        val newNts = rule.substitution.filterIsInstance<Symbol.NonTerminal>().map { it.copy() }.toList()

        // Heuristic: resolve most abstract non-terminals first
        val nextNT = (remainingNTs.toList() + newNts)
            .run {
                filter {
                    it == maxBy(Symbol.NonTerminal::abstractness)
                }
            }.randomOrNull()

        // in case newVars is null because there was no expression executed, we reuse the old local and global vars
        val newLocalvars = newVars?.filterKeys { !it.startsWith("_") } ?: localvars[currNT]
        val updatedGlobalVars = newVars?.filterKeys { it.startsWith("_") } ?: globalvars
        val updatedLocalVars = nextNT?.let {
            HashMap(localvars).apply {
                remove(currNT)
                newLocalvars?.let { x -> putAll(newNts.associateWith { x }) }
            }
        } ?: mapOf()

        return SymbolsNode(
            nextNT,
            newNts,
            this,
            updatedGlobalVars,
            updatedLocalVars, depth + 1
        )
    }
}
