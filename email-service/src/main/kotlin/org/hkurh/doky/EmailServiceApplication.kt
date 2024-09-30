package org.hkurh.doky

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver

@SpringBootApplication
class EmailServiceApplication {
    @Bean
    fun templateResolver(@Value("\${doky.email.templates.path}") templateFolder: String): ITemplateResolver {
        return ClassLoaderTemplateResolver().apply {
            prefix = templateFolder
            suffix = ".html"
            templateMode = TemplateMode.HTML
            characterEncoding = "UTF-8"
        }
    }

    @Bean
    fun templateEngine(templateResolver: ITemplateResolver?): SpringTemplateEngine {
        return SpringTemplateEngine().apply {
            setTemplateResolver(templateResolver)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<EmailServiceApplication>(*args)
}
