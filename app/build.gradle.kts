import Depends.AndroidTest.implementAndroidTest
import Depends.AndroidTest.implementEspressoTest
import Depends.Dagger.implementDagger
import Depends.Kotlin.implementKotlinForModule
import Depends.Module.implementAllModules
import Depends.Navigation.implementNavigation
import Depends.Robbie.implementRobbie
import Depends.Room.implementRoom
import Depends.Test.implementTest
import Depends.Views.implementLayouts

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
    configurations.all {
        resolutionStrategy {
            force("androidx.test:monitor:1.4.0")
        }
    }

    // =========== Robbie ==============
    implementRobbie()

    // =========== Room ==============
    implementRoom()

    // =========== Dagger ==============
    implementDagger()

    // =========== Navigation ==============
    implementNavigation()


    // TODO move to dependencies
    implementation(platform("com.google.firebase:firebase-bom:29.2.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
}