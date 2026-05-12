package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    val rightPosition = secret.zip(guess).count { (s, g) -> s == g }

    val secretCounts = secret.groupingBy { it }.eachCount().toMutableMap()
    val guessCounts = guess.groupingBy { it }.eachCount().toMutableMap()

    // Subtract exact matches so they aren't double-counted as wrong-position
    for ((s, g) in secret.zip(guess)) {
        if (s == g) {
            secretCounts[s] = secretCounts[s]!! - 1
            guessCounts[g] = guessCounts[g]!! - 1
        }
    }

    val wrongPosition = guessCounts.entries.sumOf { (ch, cnt) ->
        minOf(cnt, secretCounts.getOrDefault(ch, 0))
    }

    return Evaluation(rightPosition, wrongPosition)
}
