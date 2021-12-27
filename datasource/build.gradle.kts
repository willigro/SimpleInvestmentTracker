import Depends.Dagger.implementDagger
import Depends.Kotlin.implementKotlinForModule
import Depends.Module.implementModules
import Depends.Robbie.implementRobbie
import Depends.Room.implementRoom
import Depends.Test.implementTest


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // =========== Modules ==============
//    implementModules(Modules.common)

    // =========== Kotlin ==============
    implementKotlinForModule()

    // =========== Robbie ==============
    implementRobbie()

    // =========== Dagger ==============
    implementDagger()

    // =========== Room ==============
    implementRoom()

    // =========== Test ==============
    implementTest()
}