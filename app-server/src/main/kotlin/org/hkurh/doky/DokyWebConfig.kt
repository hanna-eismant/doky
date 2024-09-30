package org.hkurh.doky

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver

@Configuration
class DokyWebConfig : WebMvcConfigurer {

    @Value("\${doky.static.caching:true}")
    private val caching = true

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        listOf("css", "js", "woff", "woff2", "svg").forEach { ext ->
            registry.addResourceHandler("/{filename:\\.+\\.$ext}")
                .addResourceLocations("classpath:/static/")
                .resourceChain(caching)
                .addResolver(PathResourceResolver())
        }

        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
            .resourceChain(caching)
            .addResolver(object : PathResourceResolver() {
                override fun getResource(resourcePath: String, location: Resource): Resource? {
                    val requestedResource = super.getResource(resourcePath, location)
                    return requestedResource ?: super.getResource("index.html", location)
                }
            })

    }
}
