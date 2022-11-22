package isml.aidev.util

class ChainLink<T>(val value: T, var chain: Chain<T>) {
    var pred: ChainLink<T>? = null
        set(value) {
            field = value
            if (value?.succ != this) value?.succ = this
        }

    var succ: ChainLink<T>? = null
        set(value) {
            field = value
            if (value?.pred != this) value?.pred = this
        }

    fun substitute(newChain: Chain<T>?) = chain.replace(this, newChain)
}