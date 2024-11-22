# Gradle properties access reproducer

A project to reproduce unexpected access to Gradle project properties across multi-project builds.

The project sets properties in Kotlin builds and accesses them in the same and different Groovy and Kotlin build scripts.

In particular, it does not seem possible to access project properties set by 'indexed access' in a Kotlin project from
another project in the same build (i) using 'indexed access' in a Kotlin script or (ii) by any means in a Groovy project.

Logged as [Gradle issue](https://github.com/gradle/gradle/issues/31388)

To reproduce:

```run ./gradlew parentTask```

Output (Gradle 8.11.1):
```
registering in parent
registering in kotlin project
registering in groovy project

> Task :groovySubProject:groovyTask
In Groovy 'parentExtProperty' property value on root project object directly: got value 'ext'; expected to throw; FAIL
In Groovy 'parentExtraProperty' property value on root project object directly: threw MissingPropertyException; expected 'extra'; FAIL
In Groovy 'parentExtProperty' property value on root project ext object: got value 'ext'; expected to throw; FAIL
In Groovy 'parentExtraProperty' property value on root project ext object: threw MissingPropertyException; expected 'extra'; FAIL
In Groovy 'parentExtProperty' property value on root project extra object: threw MissingPropertyException; expected to throw; PASS
In Groovy 'parentExtraProperty' property value on root project extra object: threw MissingPropertyException; expected to throw; PASS

> Task :kotlinSubProject:kotlinTask
In Kotlin 'parentExtProperty' property value on root project ext object: got value 'ext' no expectation to throw PASS
In Kotlin 'parentExtProperty' property value on root project extra object: got value 'ext'; expected to throw; FAIL
In Kotlin 'parentExtraProperty' property value on root project ext object: threw UnknownPropertyException; expected to throw; PASS
In Kotlin 'parentExtraProperty' property value on root project extra object: threw UnknownPropertyException; expected 'extra'; FAIL
In Kotlin 'kotlinExtProperty' property value on Kotlin project ext object: got value 'kotlinExt' no expectation to throw PASS
In Kotlin 'kotlinExtProperty' property value on Kotlin project extra object: threw UnknownPropertyException; expected to throw; PASS
In Kotlin 'kotlinExtraProperty' property value on Kotlin project ext object: threw UnknownPropertyException no expectation PASS
In Kotlin 'kotlinExtraProperty' property value on Kotlin project extra object: got value 'kotlinExtra'; expected 'kotlinExtra'; PASS

> Task :parentTask
In root project 'parentExtProperty' property value on ext object: got value 'ext'; no expectation; PASS
In root project 'parentExtProperty' property value on extra object: threw UnknownPropertyException; expected to throw; PASS
In root project 'parentExtraProperty' property value on ext object: threw UnknownPropertyException; no expectation; PASS
In root project 'parentExtraProperty' property value on extra object: got value 'extra'; expected 'extra' PASS
```

The key lines which I would expect pass are:
- ```In Groovy 'parentExtraProperty' property value on root project object directly: threw MissingPropertyException; expected 'extra'; FAIL```
- ````In Groovy 'parentExtraProperty' property value on root project ext object: threw MissingPropertyException; expected 'extra'; FAIL````
- ```In Kotlin 'parentExtraProperty' property value on root project extra object: threw UnknownPropertyException; expected 'extra'; FAIL```