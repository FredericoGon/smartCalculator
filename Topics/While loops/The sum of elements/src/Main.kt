fun main() {
    var sum = 0
    do {
        val inputNumber = readLine()!!.toInt()
        sum = sum + inputNumber
    } while (inputNumber != 0)
    print(sum)
}