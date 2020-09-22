package tree

interface TreeConvertible<T> {
    fun findId(): T
    fun findParentId(): T?
}
