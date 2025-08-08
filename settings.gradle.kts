// Configuración de repositorios para la resolución de plugins
// (esto es lo que nos faltaba).
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

// Configuración de repositorios para las dependencias del proyecto.
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
// Nombre de la raíz del proyecto
rootProject.name = "ToDoList"
// Incluye los módulos del proyecto
include(":app")
