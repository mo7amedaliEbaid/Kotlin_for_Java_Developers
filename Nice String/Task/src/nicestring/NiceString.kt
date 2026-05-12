package nicestring

fun String.isNice(): Boolean {
    val noForbidden = !contains("bu") && !contains("ba") && !contains("be")
    val enoughVowels = count { it in "aeiou" } >= 3
    val hasDouble = zipWithNext().any { (a, b) -> a == b }

    return listOf(noForbidden, enoughVowels, hasDouble).count { it } >= 2
}