/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("com.github.bishoybasily.java-conventions")
}

dependencies {
    api(project(":springframework-commons-core"))
    implementation("org.springframework.data:spring-data-commons:2.2.11.RELEASE")
    implementation("org.springframework.data:spring-data-jpa:2.2.11.RELEASE")
    implementation("org.hibernate:hibernate-core:5.4.18.Final")
}

description = "springframework-commons :: jpa"
