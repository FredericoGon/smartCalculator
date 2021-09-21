package calculator

import java.math.BigInteger

var map = mutableMapOf<String, BigInteger>()
var postfix = mutableListOf<String>()
var postfixOperators = mutableListOf<String>()
val priorityOneOp = listOf("+", "-")
fun main() {

    do {
        val str = readLine()!!
        when {
            str == "/exit" -> break
            str == "/help" -> print("The program calculates the sum of numbers")
            str == "/map" -> printMap()
            Regex("/.*").matches(str) -> print("Unknown command")
            str.isEmpty() -> continue
            str.contains("=") -> attribution(str)
            else -> print(operate(str))
        }
    } while (true)
    print("Bye!")
}

private fun printMap() {
    for ((key, value) in map) {
        println("key: $key - value: $value")
    }
}

private fun attribution(str: String) {
    val strings = str.split("=")
    if (!Regex("[A-Za-z]*").matches(strings[0].trim())) {
        print("Invalid identifier")
    }
    if (strings.size > 2) {
        print("Invalid assignment")
    }
    val string2 = strings[1].trim()
    val number = string2.toBigIntegerOrNull()
    if (number == null) {
        if (map.containsKey(string2)) {
            map[strings[0].trim()] = map[string2] ?: BigInteger.ZERO
        } else if (strings[1].contains(Regex("[0-9]"))) {
            print("Invalid assignment")
        } else {
            print("Unknown variable")
        }
    } else {
        map[strings[0].trim()] = number
    }
}

private fun operate(line: String): String {
    val openCount = "(".count{ line.contains(it) }
    val closeCount = ")".count{ line.contains(it) }
    if (openCount != closeCount) return "Invalid expression"
    var increment = 0
    var parenthesis = 0
    for (index in line.indices) {
        if (line[index + increment].isDigit()) {
            val size = howLongIsDigit(line, index + increment)
            postfix.add(line.substring(index + increment, index + increment + size))
            increment += (size - 1)
        } else if (line[index + increment].isLetter()) {
            val size = howLongIsVariable(line, index + increment)
            val variable = line.substring(index + increment, index + increment + size)
            if (map.containsKey(variable)) {
                postfix.add(map[variable].toString())
            } else {
                return "Unknown variable"
            }
            increment += (size - 1)
        } else if (line[index + increment] == '+') {
            val size = howLongIsOperator(line, index + increment, line[index + increment])
            addLevelOneOperator("+")
            increment += (size - 1)
        } else if (line[index + increment] == '-') {
            val size = howLongIsOperator(line, index + increment, line[index + increment])
            if (size % 2 == 0) {
                addLevelOneOperator("+")
            } else {
                addLevelOneOperator("-")
            }
            increment += (size - 1)
        } else if (line[index + increment] == '/' || line[index + increment] == '*') {
            val size = howLongIsOperator(line, index + increment, line[index + increment])
            if (size > 1) {
                return "Invalid expression"
            }
            addLevelTwoOperator(line[index + increment].toString())
            increment += (size - 1)
        } else if (line[index + increment] == '(') {
            postfixOperators.add(line[index + increment].toString())
            parenthesis ++
        } else if (line[index + increment] == ')') {
            for (indexOp in postfixOperators.size - 1 downTo 0) {
                if (postfixOperators[indexOp] == "(") {
                    postfixOperators.removeAt(indexOp)
                    parenthesis --
                    break
                } else {
                    postfix.add(postfixOperators[indexOp])
                    postfixOperators.removeAt(indexOp)
                }
            }
        } else if (!line[index + increment].isWhitespace()) {
            return "Invalid expression"
        }
        if (index + increment + 1 >= line.length) {
            postfix.addAll(postfixOperators.reversed())
            postfixOperators.clear()
            break
        }
    }
    //postfix(postfix)
    if (postfix.size < 3) {
        return postfix[postfix.size - 1]
    }
    val retorno = calculatePostfix(postfix)
    postfix.clear()
    return retorno
}

fun calculatePostfix(postfix: MutableList<String>): String {
    val finalStack = mutableListOf<BigInteger>()
    for (item in postfix) {
        when (item) {
            "+" -> {
                val sum = finalStack[finalStack.size - 1] + finalStack[finalStack.size - 2]
                finalStack.removeAt(finalStack.size - 1)
                finalStack.removeAt(finalStack.size - 1)
                finalStack.add(sum)
            }
            "-" -> {
                val sum = finalStack[finalStack.size - 2] - finalStack[finalStack.size - 1]
                finalStack.removeAt(finalStack.size - 1)
                finalStack.removeAt(finalStack.size - 1)
                finalStack.add(sum)
            }
            "*" -> {
                val sum = finalStack[finalStack.size - 1] * finalStack[finalStack.size - 2]
                finalStack.removeAt(finalStack.size - 1)
                finalStack.removeAt(finalStack.size - 1)
                finalStack.add(sum)
            }
            "/" -> {
                val sum = finalStack[finalStack.size - 2] / finalStack[finalStack.size - 1]
                finalStack.removeAt(finalStack.size - 1)
                finalStack.removeAt(finalStack.size - 1)
                finalStack.add(sum)
            }
            else -> finalStack.add(item.toBigInteger())
        }
    }
    return finalStack[finalStack.size - 1].toString()
}

fun howLongIsOperator(line: String, originalIndex: Int, operator: Char): Int {
    var size = 0
    for (index in line.indices) {
        if (index + originalIndex >= line.length) {
            break
        }
        if (line[index + originalIndex] == operator) {
            size++
            continue
        } else {
            break
        }
    }
    return size
}

fun howLongIsVariable(line: String, originalIndex: Int): Int {
    var size = 0
    for (index in line.indices) {
        if (index + originalIndex >= line.length) {
            break
        }
        if (line[index + originalIndex].isLetter()) {
            size++
            continue
        } else {
            break
        }
    }
    return size
}

private fun postfix(postfix: List<String>) {
    println("Inicio do postfix")
    for (string in postfix) {
        println(string)
    }
    println("Fim do postfix")
}

fun howLongIsDigit(line: String, originalIndex: Int): Int {
    var size = 0
    for (index in line.indices) {
        if (index + originalIndex >= line.length) {
            break
        }
        if (line[index + originalIndex].isDigit()) {
            size++
            continue
        } else {
            break
        }
    }
    return size
}

fun addLevelOneOperator(operator: String) {
    if (postfixOperators.isEmpty()) {
        postfixOperators.add(operator)
    } else {
        for (index in postfixOperators.size - 1 downTo 0) {
            if (postfixOperators[index] == "(" || postfixOperators[index] == ")") {
                postfixOperators.add(operator)
                return
            } else {
                postfix.add(postfixOperators[index])
                postfixOperators.removeAt(index)
            }
        }
        postfixOperators.add(operator)
    }
}

fun addLevelTwoOperator(operator: String) {
    if (postfixOperators.isEmpty()) {
        postfixOperators.add(operator)
    } else {
        for (index in postfixOperators.size - 1 downTo 0) {
            if (priorityOneOp.contains(postfixOperators[index]) || postfixOperators[index] == "(" || postfixOperators[index] == ")"  || index == 0) {
                postfixOperators.add(operator)
                return
            } else {
                postfix.add(postfixOperators[index])
                postfixOperators.removeAt(index)
            }
        }
        postfixOperators.add(operator)
    }
}
