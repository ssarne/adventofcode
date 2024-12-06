package aoc.ktutils

fun checkForNotNull(actual: Any?, message: String = "") {
    if (actual == null) {
        System.err.println("Failure: actual=$actual  $message")
    }
}

fun check(actual: Boolean, expected: Boolean, message: String = "") {
    if (actual != expected) {
        System.err.println("Failure: actual=$actual  expected=$expected  $message")
    }
}

fun check(actual: Int, expected: Int) {
    if (actual != expected) {
        System.err.println("Failure: actual=$actual  expected=$expected")
    }
}

fun check(actual: Char, expected: Char) {
    if (actual != expected) {
        System.err.println("Failure: actual=$actual  expected=$expected")
    }
}

fun check(actual: Long, expected: Long) {
    if (actual != expected) {
        System.err.println("Failure: actual=$actual  expected=$expected")
    }
}

fun check(actual: Point3D, expected: Point3D) {
    if (actual != expected) {
        System.err.println("Failure: actual=$actual  expected=$expected")
    }
}

fun check(actual: IntArray, expected: IntArray) {
    if (actual.size != expected.size) {
        System.err.println("Failure: actual.size=${actual.size}  expected.size=${expected.size}")
        return
    }
    for (i in actual.indices) {
        if (actual[i] != expected[i]) {
            System.err.println("Failure: actual[$i]=${actual[i]}  expected[$i]=${expected[i]}")
            return
        }
    }
}

fun <T> check(actual: List<T>, expected: List<T>) {
    if (actual.size != expected.size) {
        System.err.println("Failure: actual.size=${actual.size}  expected.size=${expected.size}")
        return
    }
    for (i in actual.indices) {
        if (actual[i] != expected[i]) {
            System.err.println("Failure: actual[$i]=${actual[i]}  expected[$i]=${expected[i]}")
            return
        }
    }
}

fun check(actual: String?, expected: String?) {
    if (actual != expected) {
        System.err.println("Failure: actual=$actual  expected=$expected")
    }
}

fun checkNot(actual: String?, notAccepted: String?) {
    if (actual == notAccepted) {
        System.err.println("Failure, not accepted: $notAccepted")
    }
}

fun checkNot(actual: Int, notAccepted: Int) {
    if (actual == notAccepted) {
        System.err.println("Failure, not accepted: $notAccepted")
    }
}