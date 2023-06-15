package com.example.libslstorage.component

import com.example.libslstorage.service.TokenService
import com.example.libslstorage.util.getAccessTokenCookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.jwt.JwtValidationException
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint(
    private val tokenService: TokenService
) : Http403ForbiddenEntryPoint() {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        try {
            request.getAccessTokenCookie()
                ?.value
                ?.let { tokenService.decodeToken(it) }
        } catch (e: JwtValidationException) {
            return response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message)
        }
        super.commence(request, response, authException)
    }
}
