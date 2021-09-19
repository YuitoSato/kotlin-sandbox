package tree

import java.util.*

data class Tree<T>(
    var data: T,
    var children: MutableList<Tree<T>>
) {

    fun find(indexList: List<Int>): Tree<T>? {
        var current = this
        indexList.forEach { index ->
            val currentOpt = current.children.getOrNull(index)
            if (currentOpt == null) return currentOpt
            current = currentOpt
        }
        return current
    }

    fun <S> map(f: (T) -> S): Tree<S> {
        val stack: Stack<Pair<Tree<T>, List<Int>>> = Stack()
        val tree: Tree<S> = Tree(f(data), mutableListOf())

        children.withIndex().reversed().forEach { pair ->
            val (index, t) = pair
            stack.push(Pair(t, listOf(index)))
        }
        while (stack.isNotEmpty()) {
            val (currentTree, indexList) = stack.pop()
            val newTree = Tree(
                f(currentTree.data),
                mutableListOf()
            )
            tree.find(indexList.take(indexList.size - 1))
                ?.children?.add(newTree)
            currentTree.children.withIndex().reversed().forEach { pair ->
                val (index, t) = pair
                stack.push(Pair(t, indexList.plus(index)))
            }
        }
        return tree
    }

    fun <S> forEach(f: (T) -> S) {
        map(f)
    }

    fun <S> reduce(acc: S, f: (S, T) -> S): S {
        var result = acc
        forEach {
            result = f(result, it)
        }
        return result
    }

    companion object {
        fun <S> of(flatList: List<TreeConvertible<S>>): Tree<TreeConvertible<S>>? {
            val parentIdToChildren = flatList.groupBy { it.findParentId() }
            val rootOpt = parentIdToChildren[null]?.first()
            val treeOpt = rootOpt?.let { Tree(it, mutableListOf()) }
            val rootChildren = parentIdToChildren.getOrDefault(rootOpt?.findId(), mutableListOf())
            val stack: Stack<Pair<TreeConvertible<S>, List<Int>>> = Stack()
            rootChildren.withIndex().reversed().forEach { pair ->
                val (index, element) = pair
                stack.push(Pair(element, listOf(index)))
            }

            while (stack.isNotEmpty()) {
                val (element, indexList) = stack.pop()
                val newTree = Tree(element, mutableListOf())
                treeOpt?.find(indexList.take(indexList.size - 1))
                    ?.children?.add(newTree)
                val children = parentIdToChildren.getOrDefault(element.findId(), mutableListOf())

                children.withIndex().reversed().forEach { pair ->
                    val (index, childElement) = pair
                    stack.push(Pair(childElement, indexList.plus(index)))
                }
            }

            return treeOpt
        }
    }
}
