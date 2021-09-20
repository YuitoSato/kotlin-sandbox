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

    fun <S> mapByDFS(f: (T) -> S): Tree<S> {
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

    fun <S> mapByBFS(f: (T) -> S): Tree<S> {
        // 幅優先探索における次に処理すべきノードを格納するキュー
        val queue: Queue<Pair<Tree<T>, List<Int>>> = LinkedList()
        var tree: Tree<S>? = null

        queue += Pair(this, listOf())

        while (queue.isNotEmpty()) {
            val (currentTree, indexList) = queue.poll()
            val newTree = Tree(
                f(currentTree.data),
                mutableListOf()
            )
            if (tree == null) {
                tree = newTree
            } else {
                tree.find(indexList.take(indexList.size - 1))
                    ?.children?.add(newTree)
            }

            currentTree.children.withIndex().forEach { pair ->
                val (index, t) = pair
                queue += Pair(t, indexList.plus(index))
            }
        }
        return tree!!
    }

    fun <S> forEach(f: (T) -> S) {
        mapByDFS(f)
    }

    fun <S> reduce(acc: S, f: (S, T) -> S): S {
        var result = acc
        forEach {
            result = f(result, it)
        }
        return result
    }

    fun <S> mapRecursively(f: (T) -> S): Tree<S> {
        fun loop(tree: Tree<T>): Tree<S> {
            return Tree(f(tree.data), tree.children.map { loop(it) }.toMutableList())
        }

        return loop(this)
    }

    fun mapTree(f: (Tree<T>) -> Tree<T>): Tree<T> {
        fun loop(tree: Tree<T>): Tree<T> {
            val newTree = f(tree)
            return newTree.copy(
                children = newTree.children.map { loop(it) }.toMutableList()
            )
        }

        return loop(this)
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
