allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

val postgresqlVersion : String by project.extra
val jasyptVersion : String by project.extra 

dependencies {
    implementation(project(":moit-common"))

    api("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    // postgresql
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    // jasypt
    api("com.github.ulisesbocchio:jasypt-spring-boot-starter:$jasyptVersion")
}
