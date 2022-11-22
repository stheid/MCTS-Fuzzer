package isml.aidev

import isml.aidev.grammar.Symbol

/**
 * @param expectedSymbols The expectation (in the statistical sense) of the number of Terminals,
 * this rule will generate when applying all subsequent rules at random by the weight
 */
class RuleEdge(
    val substitution: List<Symbol>,
    val expression: String? = null,
    val weight: Float = 1.0f,
    val expectedSymbols: Float = 0.0f,
) {
    override fun toString() = "$substitution${expression ?: ""} (${weight})"
}