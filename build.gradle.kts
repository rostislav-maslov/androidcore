buildscript {
    extra.apply {
        set("kotlin_version", "1.3.50")
    }
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra.get("kotlin_version") as String}")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

// решает проблему ошибки генерации javadoc'ов при gradlew install
subprojects {
    tasks.withType<Javadoc>().all { enabled = false }
}
