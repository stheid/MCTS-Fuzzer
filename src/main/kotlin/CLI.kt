import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import isml.aidev.Algorithm
import kotlin.random.Random

class CLI : CliktCommand() {
    val maxIterations: Int by option(help = "Number of MCTS runs").int().default(10)
    val grammar: String by option(help = "Path to the grammar").default("grammars/equation_gram.yaml")

    override fun run() {
        val algo = Algorithm(maxIterations, grammar, 10, headless = false)
        repeat(maxIterations) {
            println("input: ${algo.createInput().decodeToString()}")
            algo.observe(Random.nextDouble(0.0, 1.0))
        }
        algo.join()
    }
}

fun main(args: Array<String>) = CLI().main(args)