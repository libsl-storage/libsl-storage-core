package com.example.libslstorage.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfig: WebMvcConfigurer {

    @Value("\${security.allowedOrigins}")
    private lateinit var allowedOrigins: Array<String>

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").apply {
            allowedOrigins(*allowedOrigins)
            allowedMethods("GET", "POST", "PUT", "DELETE")
            allowedHeaders("*")
            allowCredentials(true)
        }
    }
}
