package pl.macia.printinghouse.server.repository

internal class BMutableList<I, P>(
    val toBiz: (P) -> I,
    val toPer: (I) -> P,
    val list: MutableList<P> = mutableListOf()
) : MutableList<I> {


    override val size: Int by list::size
    override fun clear() = list.clear()

    override fun addAll(elements: Collection<I>): Boolean {
        val els = elements.map { toPer(it) }
        return list.addAll(els)
    }

    override fun addAll(index: Int, elements: Collection<I>): Boolean {
        val els = elements.map { toPer(it) }
        return list.addAll(index, els)
    }

    override fun add(index: Int, element: I) = list.add(index, toPer(element))

    override fun add(element: I): Boolean = list.add(toPer(element))

    override fun get(index: Int): I = toBiz(list[index])

    override fun isEmpty(): Boolean = list.isEmpty()

    override fun iterator(): MutableIterator<I> {

        return object : MutableIterator<I> {
            val i = list.iterator()
            override fun hasNext(): Boolean = i.hasNext()
            override fun next(): I = toBiz(i.next())
            override fun remove() = i.remove()
        }
    }

    override fun listIterator(): MutableListIterator<I> = listIterator(0)
    override fun listIterator(index: Int): MutableListIterator<I> {
        val iterLi = list.listIterator(index)
        return object : MutableListIterator<I> {
            override fun add(element: I) = iterLi.add(toPer(element))
            override fun hasNext(): Boolean = iterLi.hasNext()
            override fun hasPrevious(): Boolean = iterLi.hasPrevious()
            override fun next(): I = toBiz(iterLi.next())
            override fun nextIndex(): Int = iterLi.nextIndex()
            override fun previous(): I = toBiz(iterLi.previous())
            override fun previousIndex(): Int = iterLi.previousIndex()
            override fun remove() = iterLi.remove()
            override fun set(element: I) = iterLi.set(toPer(element))
        }

    }

    override fun removeAt(index: Int): I {
        return toBiz(list.removeAt(index))
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<I> {
        return list.subList(fromIndex, toIndex).map {
            toBiz(it)
        }.toMutableList()
    }

    override fun set(index: Int, element: I): I = toBiz(list.set(index, toPer(element)))

    override fun retainAll(elements: Collection<I>): Boolean {
        elements.map {
            toPer(it)
        }.apply {
            return list.retainAll(this)
        }
    }

    override fun removeAll(elements: Collection<I>): Boolean {
        var removed = false
        elements.forEach {
            if (list.remove(toPer(it)))
                removed = true

        }
        return removed
    }

    override fun remove(element: I): Boolean = list.remove(toPer(element))

    override fun lastIndexOf(element: I): Int = list.lastIndexOf(toPer(element))

    override fun indexOf(element: I): Int = list.indexOf(toPer(element))

    override fun containsAll(elements: Collection<I>): Boolean {
        elements.forEach {
            if (!this.contains(it))
                return false
        }
        return true
    }

    override fun contains(element: I): Boolean = list.contains(toPer(element))
}

internal class BMutableSet<I, P>(val toBiz: (P) -> I, val toPer: (I) -> P, val set: MutableSet<P> = mutableSetOf()) :
    MutableSet<I> {
    override fun add(element: I): Boolean = set.add(toPer(element))

    override fun addAll(elements: Collection<I>): Boolean {
        return set.addAll(elements.map { toPer(it) })
    }

    override val size: Int by set::size

    override fun clear() = set.clear()

    override fun isEmpty(): Boolean = set.isEmpty()

    override fun containsAll(elements: Collection<I>): Boolean {
        return set.containsAll(elements.map { toPer(it) })
    }

    override fun contains(element: I): Boolean = set.contains(toPer(element))

    override fun iterator(): MutableIterator<I> {
        return object : MutableIterator<I> {
            val iter = set.iterator()
            override fun hasNext(): Boolean = iter.hasNext()
            override fun next(): I = toBiz(iter.next())
            override fun remove() = iter.remove()
        }
    }

    override fun retainAll(elements: Collection<I>): Boolean {
        return elements.map {
            toPer(it)
        }.let {
            set.retainAll(it)
        }
    }

    override fun removeAll(elements: Collection<I>): Boolean {
        return elements.map {
            toPer(it)
        }.let {
            set.removeAll(it)
        }
    }

    override fun remove(element: I): Boolean = set.remove(toPer(element))

}