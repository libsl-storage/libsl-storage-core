package com.example.libslstorage.controller

import com.example.libslstorage.service.AuthenticationService
import com.example.libslstorage.util.REFRESH_TOKEN_COOKIE_NAME
import com.example.libslstorage.dto.LoginRequest
import com.example.libslstorage.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService,
    private val authService: AuthService
) {

    @Operation(
        summary = "Authenticate user",
        description = "Authenticate user by email and password",
        responses = [
            ApiResponse(responseCode = "200", description = "OK, sets cookies authFlag, accessToken, refreshToken"),
            ApiResponse(responseCode = "400", description = "Incorrect authentication data"),
        ]
    )
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest, response: HttpServletResponse) {
        val account = authenticationService.authenticateByEmailPassword(
            request.email,
            request.password
        )
        authService.createAuthCookies(account, response)
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
        authService.createAuthCookies(account, response)
    }

    @Operation(
        summary = "Refresh access token",
        description = "Authenticate user by email and password",
        security = [SecurityRequirement(name = "cookieRefresh")],
        responses = [
            ApiResponse(responseCode = "200", description = "OK, deletes cookies authFlag, accessToken, refreshToken")
        ]
    )
    @PostMapping("/logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        authService.deleteAuthCookies(request, response)
    }
}
