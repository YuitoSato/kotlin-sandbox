package tree

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

data class FlatListItemMock(
    val id: Int,
    val parentId: Int?
)

internal class TreeTest {
    private val tree = Tree(
        1,
        mutableListOf(
            Tree(
                11,
                mutableListOf(
                    Tree(111, mutableListOf()),
                    Tree(112, mutableListOf()),
                    Tree(
                        113,
                        mutableListOf(
                            Tree(1131, mutableListOf()),
                            Tree(1132, mutableListOf()),
                            Tree(1133, mutableListOf())
                        )
                    )
                )
            ),
            Tree(
                21,
                mutableListOf(
                    Tree(211, mutableListOf())
                )
            )
        )
    )

    @Test
    fun mapByDFS() {
        val newTree = tree.mapByDFS { ele -> ele + 1 }

        assertEquals(
            Tree(
                2,
                mutableListOf(
                    Tree(
                        12,
                        mutableListOf(
                            Tree(112, mutableListOf()),
                            Tree(113, mutableListOf()),
                            Tree(
                                114,
                                mutableListOf(
                                    Tree(1132, mutableListOf()),
                                    Tree(1133, mutableListOf()),
                                    Tree(1134, mutableListOf())
                                )
                            )
                        )
                    ),
                    Tree(
                        22,
                        mutableListOf(
                            Tree(212, mutableListOf())
                        )
                    )
                )
            ),
            newTree
        )
    }

    @Test
    fun mapByBFS() {
        val newTree = tree.mapByBFS { ele -> ele + 1 }

        assertEquals(
            Tree(
                2,
                mutableListOf(
                    Tree(
                        12,
                        mutableListOf(
                            Tree(112, mutableListOf()),
                            Tree(113, mutableListOf()),
                            Tree(
                                114,
                                mutableListOf(
                                    Tree(1132, mutableListOf()),
                                    Tree(1133, mutableListOf()),
                                    Tree(1134, mutableListOf())
                                )
                            )
                        )
                    ),
                    Tree(
                        22,
                        mutableListOf(
                            Tree(212, mutableListOf())
                        )
                    )
                )
            ),
            newTree
        )
    }

    @Test
    fun mapRecursively() {
        val newTree = tree.mapRecursively { ele -> ele + 1 }

        assertEquals(
            Tree(
                2,
                mutableListOf(
                    Tree(
                        12,
                        mutableListOf(
                            Tree(112, mutableListOf()),
                            Tree(113, mutableListOf()),
                            Tree(
                                114,
                                mutableListOf(
                                    Tree(1132, mutableListOf()),
                                    Tree(1133, mutableListOf()),
                                    Tree(1134, mutableListOf())
                                )
                            )
                        )
                    ),
                    Tree(
                        22,
                        mutableListOf(
                            Tree(212, mutableListOf())
                        )
                    )
                )
            ),
            newTree
        )
    }

    @Test
    fun mapRecursively_stackOverflow() {
        // スタックオーバーフローを確認するようのコード
        (1..1000).fold(Tree(1, mutableListOf()), { acc, i ->
            Tree(
                i,
                mutableListOf(acc)
            )
        }).mapRecursively { it + 1 }
    }

    @Test
    fun reduce() {
        assertEquals(
            3976, tree.reduce(0) { acc, element -> acc + element }
        )
    }

    @Test
    fun of() {
        val newTreeList = Tree.of(
            getId = { data -> data.id },
            getParentId = { data -> data.parentId },
            flatList = listOf(
                FlatListItemMock(1, null),
                FlatListItemMock(11, 1),
                FlatListItemMock(21, 1),
                FlatListItemMock(111, 11),
                FlatListItemMock(112, 11),
                FlatListItemMock(113, 11),
                FlatListItemMock(211, 21),
                FlatListItemMock(1131, 113),
                FlatListItemMock(1132, 113),
                FlatListItemMock(1134, 113)
            )
        )

        assertEquals(
            listOf(
                Tree(
                    FlatListItemMock(1, null),
                    mutableListOf(
                        Tree(
                            FlatListItemMock(11, 1),
                            mutableListOf(
                                Tree(FlatListItemMock(111, 11), mutableListOf()),
                                Tree(FlatListItemMock(112, 11), mutableListOf()),
                                Tree(
                                    FlatListItemMock(113, 11),
                                    mutableListOf(
                                        Tree(FlatListItemMock(1131, 113), mutableListOf()),
                                        Tree(FlatListItemMock(1132, 113), mutableListOf()),
                                        Tree(FlatListItemMock(1134, 113), mutableListOf())
                                    )
                                )
                            )
                        ),
                        Tree(
                            FlatListItemMock(21, 1),
                            mutableListOf(
                                Tree(FlatListItemMock(211, 21), mutableListOf())
                            )
                        )
                    )
                )
            ),
            newTreeList
        )
    }
}
