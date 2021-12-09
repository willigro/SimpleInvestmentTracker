import Depends.Dagger.implementDagger

dependencies {


    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // =========== Dagger ==============
    implementDagger()
}