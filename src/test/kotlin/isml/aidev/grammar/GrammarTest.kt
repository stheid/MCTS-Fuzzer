package isml.aidev.grammar

import org.junit.jupiter.api.Test

class GrammarTest {
    @Test
    fun readNewStyleGrammar() {
        val grammar = Grammar.fromFile(this::class.java.classLoader.getResource("xml_gen.yaml")?.path!!)
        println(grammar)
    }

    @Test
    fun readJsonGrammar() {
        val grammar = Grammar.fromFile(this::class.java.classLoader.getResource("json_simple_gram.yml")?.path!!)
        println(grammar)
    }

    @Test
    fun readSimpleGrammar() {
        val grammar = Grammar.fromFile(this::class.java.classLoader.getResource("abc.yaml")?.path!!)
        println(grammar)
    }

    @Test
    fun readAnnotGrammar() {
        val grammar =
            Grammar.fromFile(this::class.java.classLoader.getResource("simple_annotated_unconstrained.yaml")?.path!!)
        println(grammar)
    }

    @Test
    fun sampleSimpleGram() {
        val grammar = Grammar.fromFile(this::class.java.classLoader.getResource("abc.yaml")?.path!!)
        val byteseq = grammar.sample()
        println(byteseq)
    }

    @Test
    fun sampleAnotatedGram() {
        val grammar =
            Grammar.fromFile(this::class.java.classLoader.getResource("simple_annotated_unconstrained.yaml")?.path!!)
        println(grammar.sample())
        for (i in 1..100){
            println(grammar.sample())
        }

        //File("out.bin").writeBytes(byteseq.map { it.code.toByte() }.toByteArray())
    }

    @Test
    fun sampleAnotatedLocalGram() {
        val grammar =
            Grammar.fromFile(this::class.java.classLoader.getResource("simple_annotated_localvar.yaml")?.path!!)
        assert(grammar.sample() == "133355555")
        for (i in 1..100){
            println(grammar.sample())
        }
    }

    @Test
    fun sampleEquationGram() {
        val grammar =
            Grammar.fromFile(this::class.java.classLoader.getResource("equation_gram.yaml")?.path!!)
        for (i in 1..10){
            println(grammar.sample())
        }
    }

    @Test
    fun sampleAnotatedGlobalGram() {
        val grammar =
            Grammar.fromFile(this::class.java.classLoader.getResource("simple_annotated_globvar.yaml")?.path!!)
        val result = grammar.sample()
        println(result)
        assert(result.length == 5)
        for (i in 1..100){
            println(grammar.sample())
        }

        //File("out.bin").writeBytes(byteseq.map { it.code.toByte() }.toByteArray())
    }

    @Test
    fun sampleXmlAnnotGram() {
        val grammar = Grammar.fromFile(this::class.java.classLoader.getResource("xml_gen_annot.yaml")?.path!!)
        for (i in 1..500){
            val byteseq = grammar.sample()
            println(byteseq)
//            File("annotated/$i.bin").writeBytes(byteseq.map { it.code.toByte() }.toByteArray())
        }
    }

    @Test
    fun sampleJsAnnotatedGram() {
        val grammar = Grammar.fromFile(this::class.java.classLoader.getResource("js_gen.yml")?.path!!)
        for (i in 1..100){
            val byteseq = grammar.sample()
            println(byteseq)
//            File("annotated/$i.bin").writeBytes(byteseq.map { it.code.toByte() }.toByteArray())
        }
    }
}