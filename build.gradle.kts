tasks.register("parentTask") {
    println("registering in parent")
    ext["parentExtProperty"] = "ext"
    extra["parentExtraProperty"] = "extra"
    dependsOn(":groovySubProject:groovyTask", ":kotlinSubProject:kotlinTask")
    doLast {
        accessPropertyUndefinedBehaviour(
            accessDescription = "In root project 'parentExtProperty' property value on ext object",
        ) { ext["parentExtProperty"] }
        accessPropertyExpectToThrow(
            accessDescription = "In root project 'parentExtProperty' property value on extra object",
        ) { extra["parentExtProperty"] }
        accessPropertyUndefinedBehaviour(
            accessDescription = "In root project 'parentExtraProperty' property value on ext object",
        ) { ext["parentExtraProperty"] }
        accessPropertyExpectValue(
            accessDescription = "In root project 'parentExtraProperty' property value on extra object",
            expectedValue = "extra"
        ) { extra["parentExtraProperty"] }
    }
}

fun accessPropertyExpectValue(accessDescription: String, expectedValue: Any? = null, accessLambda: () -> Any?) {
    try {
        val value = accessLambda()
        val result = if (value == expectedValue) "PASS" else "FAIL"
        println("$accessDescription: got value '$value'; expected '$expectedValue' $result")
    }
    catch(t: Throwable) {
        println("$accessDescription: got threw ${t::class.simpleName}; expected '$expectedValue'; FAIL")
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
        println("$accessDescription: got value '$value'; no expectation; PASS")
    }
    catch(t: Throwable) {
        println("$accessDescription: threw ${t::class.simpleName}; no expectation; PASS")
    }
}