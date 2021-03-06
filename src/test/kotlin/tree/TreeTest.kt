package tree

import org.junit.jupiter.api.Assertions.*

internal class TreeTest {
    private val tree = Tree(1, mutableListOf(
        Tree(11, mutableListOf(
            Tree(111, mutableListOf()),
            Tree(112, mutableListOf()),
            Tree(113, mutableListOf(
                Tree(1131, mutableListOf()),
                Tree(1132, mutableListOf()),
                Tree(1133, mutableListOf())
            ))
        )),
        Tree(21, mutableListOf(
            Tree(211, mutableListOf())
        ))
    ))

    @org.junit.jupiter.api.Test
    fun map() {
        val newTree = tree.map { ele -> ele + 1 }

        assertEquals(
            Tree(2, mutableListOf(
                Tree(12, mutableListOf(
                    Tree(112, mutableListOf()),
                    Tree(113, mutableListOf()),
                    Tree(114, mutableListOf(
                        Tree(1132, mutableListOf()),
                        Tree(1133, mutableListOf()),
                        Tree(1134, mutableListOf())
                    ))
                )),
                Tree(22, mutableListOf(
                    Tree(212, mutableListOf())
                ))
            )), newTree
        )
    }

    @org.junit.jupiter.api.Test
    fun reduce() {
        assertEquals(
            3976
            ,tree.reduce(0) { acc, element -> acc + element }
        )
    }

    @org.junit.jupiter.api.Test
    fun of() {
        val newTree = Tree.of(listOf(
            TreeConvertibleMock(1, null),
            TreeConvertibleMock(11, 1),
            TreeConvertibleMock(21, 1),
            TreeConvertibleMock(111, 11),
            TreeConvertibleMock(112, 11),
            TreeConvertibleMock(113, 11),
            TreeConvertibleMock(211, 21),
            TreeConvertibleMock(1131, 113),
            TreeConvertibleMock(1132, 113),
            TreeConvertibleMock(1134, 113)
        ))

        assertEquals(Tree(TreeConvertibleMock(1, null), mutableListOf(
            Tree(TreeConvertibleMock(11, 1), mutableListOf(
                Tree(TreeConvertibleMock(111, 11), mutableListOf()),
                Tree(TreeConvertibleMock(112, 11), mutableListOf()),
                Tree(TreeConvertibleMock(113, 11), mutableListOf(
                    Tree(TreeConvertibleMock(1131, 113), mutableListOf()),
                    Tree(TreeConvertibleMock(1132, 113), mutableListOf()),
                    Tree(TreeConvertibleMock(1134, 113), mutableListOf())
                ))
            )),
            Tree(TreeConvertibleMock(21, 1), mutableListOf(
                Tree(TreeConvertibleMock(211, 21), mutableListOf())
            ))
        )), newTree)
    }
}
