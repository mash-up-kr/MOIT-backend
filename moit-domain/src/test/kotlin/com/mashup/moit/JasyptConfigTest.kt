package com.mashup.moit

import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
@Disabled
class JasyptConfigTest {

    @Value("\${jasypt.encryptor.password}")
    lateinit var jasyptEncryptorPassword: String

    @Autowired
    lateinit var configurableEnvironment: ConfigurableEnvironment
    lateinit var encryptor: DefaultLazyEncryptor

    @BeforeEach
    internal fun setUp() {
        check(jasyptEncryptorPassword.isNotBlank()) {
            "jasypt.encryptor.password must not be null, empty or blank. "
        }
        encryptor = DefaultLazyEncryptor(configurableEnvironment)
    }

    @Test
    fun testForEncryption() {
        val source = "test"
        println("source: $source")
        println("encrypted: ${encryptor.encrypt(source)}")
    }

    @Test
    fun testForDecryption() {
        val source = "test"
        println("source: $source")
        println("decrypted: ${encryptor.decrypt(source)}")
    }
}
