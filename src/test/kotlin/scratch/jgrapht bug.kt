import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.nio.dot.DOTExporter
import java.io.File


class Node(val value: StringWrap)
class StringWrap(var value: String)

fun main() {
    val graph = DefaultDirectedGraph<Node, DefaultEdge>(DefaultEdge::class.java)
    val node1 = Node(StringWrap("old"))
    graph.addVertex(node1)

    val exporter = DOTExporter<Node, DefaultEdge> { """"${it.value.value}"""" }
    exporter.exportGraph(graph, File("old.dot").bufferedWriter())
    node1.value.value = "new"
    exporter.exportGraph(graph, File("new.dot").bufferedWriter())
}