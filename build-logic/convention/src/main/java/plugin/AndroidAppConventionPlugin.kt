package plugin

import AndroidConfigConventions
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("unused")
class AndroidAppConventionPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            pluginManager.applyPlugin()
            extensions.configure<ApplicationExtension> {
                configureAndroid()
                configureKotlin(this)
            }
            applyDependencies()
        }
    }

    private fun PluginManager.applyPlugin() {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")
        apply("kotlin-kapt")
        apply("kotlin-parcelize")
        apply("com.google.devtools.ksp")
    }

    private fun ApplicationExtension.configureAndroid() {
        configureAndroidCommon()

        defaultConfig.targetSdk = AndroidConfigConventions.TARGET_SDK_VERSION

        buildTypes {
            release {
                isMinifyEnabled = true
                isShrinkResources = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
    }

    private fun Project.applyDependencies() {
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
            implementationDefaultTestDependencies(libs)
        }
    }

}