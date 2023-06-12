package com.example.libslstorage

import com.example.libslstorage.util.ACCESS_TOKEN_COOKIE_NAME
import com.example.libslstorage.util.REFRESH_TOKEN_COOKIE_NAME
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@OpenAPIDefinition(info = Info(title = "LibSL Storage API", version = "0.0.1"))
@SecurityScheme(
    name = "cookieAuth",
    scheme = ACCESS_TOKEN_COOKIE_NAME,
    type = SecuritySchemeType.APIKEY,
    `in` = SecuritySchemeIn.COOKIE
)
@SecurityScheme(
    name = "cookieRefresh",
    scheme = REFRESH_TOKEN_COOKIE_NAME,
    type = SecuritySchemeType.APIKEY,
    `in` = SecuritySchemeIn.COOKIE
)
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
