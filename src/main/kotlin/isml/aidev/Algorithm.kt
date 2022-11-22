package isml.aidev

import ai.libs.jaicore.graphvisualizer.plugin.graphview.GraphViewPlugin
import ai.libs.jaicore.graphvisualizer.plugin.nodeinfo.NodeInfoGUIPlugin
import ai.libs.jaicore.graphvisualizer.plugin.nodeinfo.NodeInfoGenerator
import ai.libs.jaicore.graphvisualizer.window.AlgorithmVisualizationWindow
import ai.libs.jaicore.search.algorithms.mdp.mcts.uct.UCTFactory
import ai.libs.jaicore.search.algorithms.standard.mcts.MCTSPathSearchFactory
import ai.libs.jaicore.search.model.other.EvaluatedSearchGraphPath
import ai.libs.jaicore.search.probleminputs.GraphSearchWithPathEvaluationsInput
import isml.aidev.grammar.Grammar
import isml.aidev.starlib.PCFGSearchInput
import isml.aidev.util.toWord
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class Algorithm(
    maxIterations: Int = 100,
    grammarPath: String = "grammar.yaml",
    maxPathLength: Int = 20,
    headless: Boolean = true,
) {
    private val inputChannel = Channel<ByteArray>()
    private val covChannel = Channel<Double>()
    private var worker: Thread
    private lateinit var solution: EvaluatedSearchGraphPath<SymbolsNode, RuleEdge, Double>

    init {
        // construct a search problem that is in alignment to the graphgenerator and so on
        val rawInput = PCFGSearchInput(
            // parse grammar to object representation of the grammar
            Grammar.fromFile(grammarPath)
        )

        // create attach an evaluator to it
        val problem = GraphSearchWithPathEvaluationsInput(rawInput) {
            // this function is executed after each input sampled through the MCTS
            runBlocking {
                inputChannel.send(it.toWord().toByteArray())
                covChannel.receive()
            }
        }

        // create MCTS algorithm
        val factory = MCTSPathSearchFactory<SymbolsNode, RuleEdge>()
        val uct = UCTFactory<SymbolsNode, RuleEdge>().withDefaultPolicy { symbols, rules ->
            if (symbols.depth < maxPathLength) {
                rules.toList().choice(rules.map { it.weight.toDouble() }.toDoubleArray().normalize())
            } else {
                rules.minBy { it.expectedSymbols }
            }
        }
        uct.withMaxIterations(maxIterations)
        val mcts = factory.withMCTSFactory(uct).withProblem(problem).algorithm

        if (!headless) {
            val window = AlgorithmVisualizationWindow(mcts)
            window.withMainPlugin(GraphViewPlugin())
            window.withPlugin(NodeInfoGUIPlugin(object : NodeInfoGenerator<SymbolsNode> {
                override fun getName() = ""
                override fun generateInfoForNode(node: SymbolsNode?) = node.toString()
            }))
        }

        // start mcts call in background
        worker = thread { solution = mcts.call() }
    }

    fun createInput(): ByteArray {
        // if available memory is below .5% of the total memory
        val thresh = Runtime.getRuntime().maxMemory() / 200
        if (Runtime.getRuntime().freeMemory() < thresh && Runtime.getRuntime().run { totalMemory() == maxMemory() }) {
            println(
                "MCTS fuzzer has been killed because available Memory is less than ${
                    thresh / 1000000
                } MB"
            )
            exitProcess(1)
        }
        return runBlocking { inputChannel.receive() }
    }

    fun observe(reward: Double) =
        runBlocking { covChannel.send(reward) }

    fun join() {
        worker.join()

        println(solution.toWord())
        println(solution.score)
    }
}