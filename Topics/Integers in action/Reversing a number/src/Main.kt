fun main() {
    val inputNumber = readLine()!!.toInt()
    val lastNumber = inputNumber % 10
    val middleNumber = (inputNumber/10) % 10
    val firstNumber = (inputNumber/100)
    print(lastNumber)
    print(middleNumber)
    print(firstNumber)
}