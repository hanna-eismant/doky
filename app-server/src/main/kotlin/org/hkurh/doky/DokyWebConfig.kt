/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see [Hyperlink removed
 * for security reasons]().
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

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
