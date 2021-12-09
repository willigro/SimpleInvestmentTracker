import Depends.AndroidTest.implementAndroidTest
import Depends.AndroidTest.implementEspressoTest
import Depends.Dagger.implementDagger
import Depends.Kotlin.implementKotlinForModule
import Depends.Module.implementAllModules
import Depends.Robbie.implementLocalRobbie
import Depends.Room.implementRoom
import Depends.Test.implementTest
import Depends.Views.implementLayouts

android {
    buildFeatures.dataBinding = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // =========== Modules ==============
    implementAllModules(Modules.app)

    // =========== Kotlin ==============
    implementKotlinForModule()

    // =========== AppCompat ==============
    implementation(Depends.AppCompat.getAppcompat())

    // =========== Material ==============
    implementation(Depends.Material.getMaterial())

    // =========== View ==============
    implementLayouts()

    // =========== Test ==============
    implementTest()
    implementEspressoTest()
    implementAndroidTest()

    // =========== Robbie ==============
    implementLocalRobbie()

    // =========== Room ==============
    implementRoom()

    // =========== Dagger ==============
    implementDagger()
}