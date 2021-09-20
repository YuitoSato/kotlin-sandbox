package tree

class Playground {

    fun doSomething() {
        val list = mutableListOf("a", "b", "c")

        tailrec fun loop(list: MutableList<String>) {
            if (list.size > 0) {
                val first = list.removeAt(0)
                println(first)
            }
            if (list.size > 0) {
                loop(list)
            }
        }

        loop(list)
    }
}
