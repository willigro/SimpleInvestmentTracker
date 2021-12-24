import Depends.AndroidTest.implementEspressoTest
import Depends.Dagger.implementDagger
import Depends.Kotlin.implementKotlinForModule
import Depends.Module.implementModules
import Depends.Robbie.implementRobbie
import Depends.Test.implementTest
import Depends.ViewModel.implementViewModel

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // =========== Modules ==============
    implementModules(Modules.datasource)

    // =========== Kotlin ==============
    implementKotlinForModule()

    // =========== AppCompat ==============
    implementation(Depends.AppCompat.getAppcompat())

    // =========== ViewModel ==============
    implementViewModel()

    // =========== Dagger ==============
    implementDagger()

    // =========== Robbie ==============
    implementRobbie()

    // =========== Material ==============
    implementation(Depends.Material.getMaterial())

    // =========== Test ==============
    implementTest()
    implementEspressoTest()
    implementation(Depends.AndroidTest.ESPRESSO_IDLING)
    debugImplementation(Depends.AndroidTest.FRAGMENT_TESTING)
}