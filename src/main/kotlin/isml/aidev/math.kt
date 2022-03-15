package isml.aidev

import kotlin.random.Random

fun DoubleArray.normalize(): DoubleArray {
    val sum = this.sum()
    return this.map { it / sum }.toDoubleArray()
}

fun DoubleArray.cumsum(): DoubleArray {
    var curr = 0.0
    return this.map {
        curr += it
        return@map curr
    }.toDoubleArray()
}

fun <T> List<T>.choice(p: DoubleArray): T {
    val rand = Random.nextDouble()
    val pcumsum = p.cumsum()
    return this.withIndex().first { pcumsum.elementAt(it.index) > rand }.value
}