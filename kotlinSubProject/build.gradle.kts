tasks.register("kotlinTask") {
    println("registering in kotlin project")
    ext["kotlinExtProperty"] = "kotlinExt"
    extra["kotlinExtraProperty"] = "kotlinExtra"
    doLast {
        accessPropertyUndefinedBehaviour(
            accessDescription = "In Kotlin 'parentExtProperty' property value on root project ext object"
        ) { rootProject.ext["parentExtProperty"] }
        accessPropertyExpectToThrow(
            accessDescription = "In Kotlin 'parentExtProperty' property value on root project extra object",
        ) { rootProject.extra["parentExtProperty"] }
        accessPropertyExpectToThrow(
            accessDescription = "In Kotlin 'parentExtraProperty' property value on root project ext object",
        ) { rootProject.ext["parentExtraProperty"] }
        accessPropertyExpectValue(
            accessDescription = "In Kotlin 'parentExtraProperty' property value on root project extra object",
            expectedValue = "extra"
        ) { rootProject.extra["parentExtraProperty"] }
        accessPropertyUndefinedBehaviour(
            accessDescription = "In Kotlin 'kotlinExtProperty' property value on Kotlin project ext object",
        ) { ext["kotlinExtProperty"] }
        accessPropertyExpectToThrow(
            accessDescription = "In Kotlin 'kotlinExtProperty' property value on Kotlin project extra object",
        ) { extra["kotlinExtProperty"] }
        accessPropertyUndefinedBehaviour(
            accessDescription = "In Kotlin 'kotlinExtraProperty' property value on Kotlin project ext object",
        ) { ext["kotlinExtraProperty"] }
        accessPropertyExpectValue(
            accessDescription = "In Kotlin 'kotlinExtraProperty' property value on Kotlin project extra object",
            expectedValue = "kotlinExtra"
        ) { extra["kotlinExtraProperty"] }
    }
}

fun accessPropertyExpectValue(accessDescription: String, expectedValue: Any? = null, accessLambda: () -> Any?) {
    try {
        val value = accessLambda()
        val result = if (value == expectedValue) "PASS" else "FAIL"
        println("$accessDescription: got value '$value'; expected '$expectedValue'; $result")
    }
    catch(t: Throwable) {
        println("$accessDescription: threw ${t::class.simpleName}; expected '$expectedValue'; FAIL")
    }
}

fun accessPropertyExpectToThrow(accessDescription: String, accessLambda: () -> Any?) {
    try {
        val value = accessLambda()
        println("$accessDescription: got value '$value'; expected to throw; FAIL")
    }
    catch(t: Throwable) {
        println("$accessDescription: threw ${t::class.simpleName}; expected to throw; PASS")
    }
}

fun accessPropertyUndefinedBehaviour(accessDescription: String, accessLambda: () -> Any?) {
    try {
        val value = accessLambda()
        println("$accessDescription: got value '$value' no expectation to throw PASS")
    }
    catch(t: Throwable) {
        println("$accessDescription: threw ${t::class.simpleName} no expectation PASS")
    }
}