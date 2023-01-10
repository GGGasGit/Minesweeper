import kotlin.math.*

fun main() {
    val (x1, y1) = readln().split(" ").map { it.toDouble() }
    val (x2, y2) = readln().split(" ").map { it.toDouble() }

    val length1 = sqrt(x1.pow(2) + y1.pow(2))
    val length2 = sqrt(x2.pow(2) + y2.pow(2))
    val dotProduct = x1 * x2 + y1 * y2

    val angle = acos(dotProduct / (length1 * length2)) / PI * 180

    print(angle)
}