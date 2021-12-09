import Depends.Dagger.implementDagger
import Depends.Kotlin.implementKotlinForModule
import Depends.Module.implementModules
import Depends.Robbie.implementLocalRobbie
import Depends.Room.implementRoom
import Depends.Test.implementTest

android {
    buildTypes.forEach {
        it.buildConfigField("String", "BASE_NAME", "\"investmenttracks.db\"")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // =========== Modules ==============
    implementModules(Modules.common)

    // =========== Kotlin ==============
    implementKotlinForModule()

    // =========== Robbie ==============
    implementLocalRobbie()

    // =========== Dagger ==============
    implementDagger()

    // =========== Room ==============
    implementRoom()

    // =========== Test ==============
    implementTest()
}