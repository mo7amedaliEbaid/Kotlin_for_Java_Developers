package rationals

import java.math.BigInteger

data class Rational(val numerator: BigInteger, val denominator: BigInteger) : Comparable<Rational> {

    init {
        require(denominator != BigInteger.ZERO) { "Denominator cannot be zero" }
    }

    private fun normalized(): Rational {
        val gcd = numerator.gcd(denominator)
        val sign = if (denominator < BigInteger.ZERO) BigInteger.ONE.negate() else BigInteger.ONE
        return Rational(sign * numerator / gcd, sign * denominator / gcd)
    }

    operator fun plus(other: Rational): Rational {
        val n = numerator * other.denominator + other.numerator * denominator
        return Rational(n, denominator * other.denominator).normalized()
    }

    operator fun minus(other: Rational): Rational {
        val n = numerator * other.denominator - other.numerator * denominator
        return Rational(n, denominator * other.denominator).normalized()
    }

    operator fun times(other: Rational): Rational =
        Rational(numerator * other.numerator, denominator * other.denominator).normalized()

    operator fun div(other: Rational): Rational =
        Rational(numerator * other.denominator, denominator * other.numerator).normalized()

    operator fun unaryMinus(): Rational = Rational(-numerator, denominator)

    override fun compareTo(other: Rational): Int =
        (numerator * other.denominator).compareTo(other.numerator * denominator)

    override fun equals(other: Any?): Boolean {
        if (other !is Rational) return false
        val a = normalized()
        val b = other.normalized()
        return a.numerator == b.numerator && a.denominator == b.denominator
    }

    override fun hashCode(): Int {
        val n = normalized()
        return 31 * n.numerator.hashCode() + n.denominator.hashCode()
    }

    override fun toString(): String {
        val n = normalized()
        return if (n.denominator == BigInteger.ONE) "${n.numerator}" else "${n.numerator}/${n.denominator}"
    }
}

fun String.toRational(): Rational {
    val parts = split("/")
    return if (parts.size == 1)
        Rational(toBigInteger(), BigInteger.ONE)
    else
        Rational(parts[0].toBigInteger(), parts[1].toBigInteger()).let {
            Rational(it.numerator, it.denominator)
        }
}

infix fun Int.divBy(other: Int): Rational = Rational(toBigInteger(), other.toBigInteger())
infix fun Long.divBy(other: Long): Rational = Rational(toBigInteger(), other.toBigInteger())
infix fun BigInteger.divBy(other: BigInteger): Rational = Rational(this, other)

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println(
        "912016490186296920119201192141970416029".toBigInteger() divBy
                "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2
    )
}