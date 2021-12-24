import Depends.AndroidTest.implementEspressoTestAllInDebug
import Depends.Kotlin.implementKotlinForModule
import Depends.Module.implementModules
import Depends.Test.implementAllInDebugTest

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // =========== Modules ==============
    implementModules(Modules.common, Modules.datasource)

    // =========== Kotlin ==============
    implementKotlinForModule()

    // =========== Test ==============
    implementAllInDebugTest()
    implementEspressoTestAllInDebug()
    debugImplementation(Depends.AndroidTest.ESPRESSO_IDLING)
    debugImplementation(Depends.AndroidTest.FRAGMENT_TESTING)
    debugImplementation(Depends.AndroidTest.COROUTINES_TEST)
}