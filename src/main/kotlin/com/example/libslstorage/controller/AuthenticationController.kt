package com.example.libslstorage.controller

import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.service.AuthenticationService
import com.example.libslstorage.service.CookieService
import com.example.libslstorage.util.REFRESH_TOKEN_COOKIE_NAME
import com.example.libslstorage.dto.LoginRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val cookieService: CookieService,
    private val authenticationService: AuthenticationService
) {

    private fun createAuthCookies(account: AccountEntity, response: HttpServletResponse) {
        cookieService.createAuthCookies(account).forEach { response.addCookie(it) }
    }

    @Operation(
        summary = "Authenticate user",
        description = "Authenticate user by email and password",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "401", description = "Incorrect authentication data"),
        ]
    )
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest, response: HttpServletResponse) {
        val account = authenticationService.authenticateByEmailPassword(
            request.email,
            request.password
        )
        createAuthCookies(account, response)
    }

    @Operation(
        summary = "Refresh access token",
        description = "Authenticate user by email and password",
        security = [SecurityRequirement(name = "cookieRefresh")]
    )
    @PostMapping("/refresh")
    fun refresh(
        @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME) refreshToken: String,
        response: HttpServletResponse
    ) {
        val account = authenticationService.authenticateByJwt(refreshToken)
        createAuthCookies(account, response)
    }
}
