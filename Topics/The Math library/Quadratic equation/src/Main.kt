import kotlin.math.*

fun main() {
    val a = readln().toDouble()
    val b = readln().toDouble()
    val c = readln().toDouble()

    val discriminantSquareRoot = sqrt(b.pow(2) - 4 * a * c)
    val x1 = (-b + discriminantSquareRoot) / (2 * a)
    val x2 = (-b - discriminantSquareRoot) / (2 * a)

    print(if (x1 < x2) "$x1 $x2" else "$x2 $x1")
}