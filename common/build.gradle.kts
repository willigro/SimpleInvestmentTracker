import Depends.AndroidTest.implementEspressoTest
import Depends.Dagger.implementDagger
import Depends.Kotlin.implementKotlinForModule
import Depends.Module.implementModules
import Depends.Robbie.implementRobbie
import Depends.Room.implementRoom
import Depends.Test.implementTest
import Depends.ViewModel.implementViewModel

android {
    buildTypes.forEach {
        it.buildConfigField("String", "BASE_NAME", "\"investmenttracks.db\"")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

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

    // =========== Room ==============
    implementRoom()
}