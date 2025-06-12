pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
//        maven { url = uri("https://jitpack.io") }
        maven("https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
//        maven { url = uri("https://jitpack.io") }
        maven("https://jitpack.io")
    }
}

rootProject.name = "EduHubConnect"
include(":app")
 