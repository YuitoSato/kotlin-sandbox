package tree

data class TreeConvertibleMock(
    val id: Int,
    val parentId: Int?
): TreeConvertible<Int> {
    override fun findId(): Int {
        return id
    }

    override fun findParentId(): Int? {
        return parentId
    }
}
