plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.vanniktech.publish)
}

val version: String = "1.0.0"

group = "io.github.mltheuser"

repositories {
    mavenCentral()
}

kotlin {
    // 1. The JVM target (Server, Desktop Java)
    jvm()
    
    // 2. The iOS Targets (Required for iPhone/iPad support)
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    
    // 3. Web Targets
    js {
        browser()
        nodejs()
    }
    wasmJs {
        browser()
        nodejs()
    }

    // 4. Linux
    linuxX64()
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ksoup.html)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
    
    signAllPublications()
    
    coordinates("io.github.mltheuser", "khtmltomarkdown", version)
    
    pom {
        name.set("KHtmlToMarkdown")
        description.set("A Kotlin Multiplatform library for converting HTML to Markdown")
        inceptionYear.set("2025")
        url.set("https://github.com/mltheuser/KHtmlToMarkdown")
        
        licenses {
            license {
                name.set("MIT LICENSE")
                url.set("https://opensource.org/license/mit")
                distribution.set("https://opensource.org/license/mit")
            }
        }
        
        developers {
            developer {
                id.set("mltheuser")
                name.set("Malte Heuser")
                url.set("https://github.com/mltheuser/")
            }
        }
        
        scm {
            url.set("https://github.com/mltheuser/KHtmlToMarkdown")
            connection.set("scm:git:git://github.com/mltheuser/KHtmlToMarkdown.git")
            developerConnection.set("scm:git:ssh://git@github.com/mltheuser/KHtmlToMarkdown.git")
        }
    }
}