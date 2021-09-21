fun main() {
    var count = 0
    do {
        val inputNumber = readLine()!!.toInt()
        if (inputNumber != 0) count++
    } while (inputNumber != 0)
    print(count)
}