import com.google.cloud.tools.jib.gradle.JibExtension

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

val swaggerVersion: String by project.extra
val jjwtVersion: String by project.extra

apply(plugin = "com.google.cloud.tools.jib")

dependencies {
    implementation(project(":moit-domain"))
    implementation(project(":moit-common"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    testImplementation("org.springframework.security:spring-security-test")

    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$swaggerVersion")
}

configure<JibExtension> {
    val registryUsername = System.getenv("DOCKERHUB_USERNAME")
    val (activeProfile, containerImageName) = getProfileAndImageName(registryUsername)

    from {
        image = "eclipse-temurin:17-jre"
    }

    to {
        image = containerImageName
        tags = setOf("$version", "latest")
        auth {
            username = registryUsername
            password = System.getenv("DOCKERHUB_PASSWORD")
        }
    }

    container {
        // TODO: 서버 스펙에 따라 Xmx/Xms, Initial/Min/MaxRAMFraction 설정
        jvmFlags = listOf(
            "-server",
            "-XX:+UseContainerSupport",
            "-XX:+UseStringDeduplication",
            "-Dserver.port=8080",
            "-Dfile.encoding=UTF-8",
            "-Djava.awt.headless=true",
            "-Dspring.profiles.active=${activeProfile}",
        )
        ports = listOf("8080")
    }
}

fun getProfileAndImageName(registryUsername: String?): Array<String> {
    val containerImageName = "${registryUsername}/${project.name}"
    if (project.hasProperty("release")) {
        return arrayOf("release", containerImageName)
    }
    return arrayOf("dev", "$containerImageName-dev")
}
