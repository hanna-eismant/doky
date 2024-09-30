package org.hkurh.doky

import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.crypto.spec.SecretKeySpec


@EnableScheduling
@EnableAsync
@SpringBootApplication
class DokyApplication {
    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    companion object {
        private const val SECRET_KEY = "dokySecretKey-hanna.kurhuzenkava-project"
        val SECRET_KEY_SPEC = SecretKeySpec(SECRET_KEY.toByteArray(), SignatureAlgorithm.HS256.jcaName)

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(DokyApplication::class.java, *args)
        }
    }
}
