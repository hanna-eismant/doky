package org.hkurh.doky

import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver
import javax.crypto.spec.SecretKeySpec


@SpringBootApplication
class DokyApplication {
    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun templateResolver(@Value("\${doky.email.templates.path}") templateFolder: String): ITemplateResolver {
        return ClassLoaderTemplateResolver().apply {
            prefix = templateFolder
            suffix = ".html"
            setTemplateMode(TemplateMode.HTML)
            characterEncoding = "UTF-8"
        }
    }

    @Bean
    fun templateEngine(templateResolver: ITemplateResolver?): SpringTemplateEngine {
        return SpringTemplateEngine().apply {
            setTemplateResolver(templateResolver)
        }
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
