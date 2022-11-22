package isml.aidev

import isml.aidev.grammar.Symbol
import org.junit.jupiter.api.Test

internal class SymbolTest {
    private val nt = Symbol.NonTerminal("f")

    @Test
    fun ntEquality() = assert(nt == nt)

    @Test
    fun uniqueNtEquality() = assert(nt != nt.copy())

    @Test
    fun uniqueNtEquality2() {
        val uniqueNT = nt.copy()
        assert(uniqueNT == uniqueNT)
    }
}

