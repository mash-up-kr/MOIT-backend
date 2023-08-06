import com.google.cloud.tools.jib.gradle.JibExtension

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

val swaggerVersion: String by project.extra
val jjwtVersion: String by project.extra
val awsVersion: String by project.extra
val firebaseVersion: String by project.extra
val appenderVersion: String by project.extra 

apply(plugin = "com.google.cloud.tools.jib")

dependencies {
    implementation(project(":moit-domain"))
    implementation(project(":moit-common"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    
    // security 
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    testImplementation("org.springframework.security:spring-security-test")
    
    // aws
    implementation("org.springframework.cloud:spring-cloud-starter-aws:$awsVersion")
    
    // jwt 
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
    
    // fcm 
    implementation ("com.google.firebase:firebase-admin:$firebaseVersion")
    
    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$swaggerVersion")
    
    // kafka
    implementation("org.springframework.kafka:spring-kafka")
    
    // cloudwatch
    implementation ("ca.pjer:logback-awslogs-appender:$appenderVersion")
}

configure<JibExtension> {
    val registryUsername = System.getenv("DOCKERHUB_USERNAME")
    val jasyptPassword = System.getenv("JASYPT_ENCRYPTOR_PASSWORD")
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
            "-Djasypt.encryptor.password=${jasyptPassword}"
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
