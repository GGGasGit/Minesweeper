import kotlin.math.*

fun main() {
    val number = readln().toDouble()
    print(floor((number - floor(number)) * 10.0).toInt())
}