package scratch

import isml.aidev.util.Evaluator

fun main() {
    println(Evaluator.eval("x<5", mapOf("x" to 10)))
    println(Evaluator.eval("1==1", mapOf("x" to 10)))
    println(Evaluator.exec("del i", mapOf("i" to 10)))
    println(Evaluator.exec("y+=1"))
}
