import Depends.Dagger.implementDagger
import Depends.Kotlin.implementKotlinForModule
import Depends.Module.implementAllModules
import Depends.ViewModel.implementViewModel

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // =========== Modules ==============
//    implementAllModules(Modules.app, Modules.di)

    // =========== Kotlin ==============
    implementKotlinForModule()

    // =========== ViewModel ==============
    implementViewModel()

    // =========== Dagger ==============
    implementDagger()
}