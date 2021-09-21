fun main() {
    var higher = 0
    do {
        val inputNumber = readLine()!!.toInt()
        if (inputNumber > higher) {
            higher = inputNumber
        }    
    } while (inputNumber != 0)
    print(higher)
}
