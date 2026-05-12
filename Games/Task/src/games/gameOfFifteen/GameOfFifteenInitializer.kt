package games.gameOfFifteen

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialized the first 15 cells on a board.
     * The last cell is empty.
     */
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    /*
     * Generate a random permutation from 1 to 15.
     * `shuffled()` function might be helpful.
     * If the permutation is not even, make it even (for instance,
     * by swapping two numbers).
     */
    override val initialPermutation by lazy {
        val perm = (1..15).shuffled().toMutableList()
        if (!isEven(perm)) {
            // swapping the last two elements changes parity by exactly one inversion
            val tmp = perm[13]; perm[13] = perm[14]; perm[14] = tmp
        }
        perm
    }
}

