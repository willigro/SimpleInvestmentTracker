import Depends.AndroidTest.implementAndroidTest
import Depends.AndroidTest.implementEspressoTest
import Depends.Dagger.implementDagger
import Depends.Databinding.implementDatabinding
import Depends.Kotlin.implementKotlinForModule
import Depends.LegacySupport.implementLegacySupport
import Depends.Module.androidTestImplementationModules
import Depends.Module.implementModules
import Depends.Module.testImplementationModules
import Depends.Robbie.implementRobbie
import Depends.Room.implementRoom
import Depends.Tags.implementTags
import Depends.Test.implementTest
import Depends.ViewModel.implementViewModel
import Depends.Views.implementLayouts

plugins {
    id("kotlin-android")
}
android {
    defaultConfig {
        testInstrumentationRunner("com.rittmann.crypto.MockTestRunner")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementLegacySupport()

    // =========== Modules ==============
    implementModules(Modules.common, Modules.diUtils, Modules.datasource, Modules.deposit)
    androidTestImplementationModules(Modules.commonTest)
    testImplementationModules(Modules.commonTest)

    // =========== Kotlin ==============
    implementKotlinForModule()

    // =========== AppCompat ==============
    implementation(Depends.AppCompat.getAppcompat())

    // =========== Material ==============
    implementation(Depends.Material.getMaterial())

    // =========== View ==============
    implementLayouts()
    implementDatabinding()
    implementTags()

    // =========== Test ==============
    implementTest()
    implementEspressoTest()
    implementAndroidTest()

    // =========== Robbie ==============
    implementRobbie()

    // =========== Room ==============
    implementRoom()

    // =========== Dagger ==============
    implementDagger()

    // =========== ViewModel ==============
    implementViewModel()
}