fun main() {
    val classSize1 = readLine()!!.toInt()
    val classSize2 = readLine()!!.toInt()
    val classSize3 = readLine()!!.toInt()
    val desksClass1 = getDesks(classSize1)
    val desksClass2 = getDesks(classSize2)
    val desksClass3 = getDesks(classSize3)
    val sum = desksClass1 + desksClass2 + desksClass3
    print(sum)
}

fun getDesks(classSize: Int): Int {
    return if (classSize % 2 == 0) {
        (classSize/2)
    } else {
        (classSize+1)/2
    }
}