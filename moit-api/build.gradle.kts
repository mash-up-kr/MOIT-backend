tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":moit-domain"))
    implementation(project(":moit-common"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}
