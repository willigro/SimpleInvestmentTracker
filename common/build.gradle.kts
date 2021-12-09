import Depends.AndroidTest.implementEspressoTest
import Depends.Dagger.implementDagger
import Depends.Kotlin.implementKotlinForModule
import Depends.Robbie.implementLocalRobbie
import Depends.Test.implementTest
import Depends.ViewModel.implementViewModel

android {
    buildFeatures.dataBinding = true
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
    implementLocalRobbie()

    // =========== Material ==============
    implementation(Depends.Material.getMaterial())

    // =========== Test ==============
    implementTest()
    implementEspressoTest()
    implementation(Depends.AndroidTest.ESPRESSO_IDLING)
    debugImplementation(Depends.AndroidTest.FRAGMENT_TESTING)
}