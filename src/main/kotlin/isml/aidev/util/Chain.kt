package isml.aidev.util

class Chain<T>(list: List<T>) : Sequence<T> {
    var first: ChainLink<T>?
    var last: ChainLink<T>?

    init {
        // create a link for each individual and then link them
        list
            .map { ChainLink(it, this) }
            .also {
                first = it.first()
                last = it.last()
            }
            .zipWithNext()
            .forEach { (a, b) ->
                a.succ = b // automatically adds back reference
            }
    }

    override fun iterator() = object : Iterator<T> {
        var node = first

        override fun hasNext() =
            node != null

        override fun next() =
            // return current and update reference
            node?.apply { node = succ }!!.value

    }

    fun linkIterator() = object : Iterator<ChainLink<T>> {
        var node = first

        override fun hasNext() =
            node != null

        override fun next() =
            // return current and update reference
            node?.apply { node = succ }!!
    }

    /**
     * substitute "this" with a chain of links
     *
     * will basically clear all references to "this" and nit in references to the start and end of chain.
     * @return return the provided chain object embedded in the larger chain
     */
    fun replace(oldLink: ChainLink<T>, newChain: Chain<T>?) {
        if (oldLink.chain != this) {
            throw IllegalArgumentException("The provided ChainLink is not part of the chain. Can't substitute")
        }

        // substitute all references to oldLink with references to the new chain, or directly remove the link if chain null
        // either overwrite predecessors succ-pointer or the chain.first pointer
        oldLink.pred
            ?.apply { succ = newChain?.first ?: oldLink.succ }
            ?: run { first = newChain?.first ?: oldLink.succ }

        oldLink.succ
            ?.also { it.pred = newChain?.last ?: oldLink.pred }
            ?: run { last = newChain?.last ?: oldLink.pred }

        // substitute all references to the newChain object with this chain
        newChain?.linkIterator()?.forEach {
            it.chain = this
        }
    }

    override fun toString() =
        iterator().asSequence().joinToString(separator = " <-> ")
}