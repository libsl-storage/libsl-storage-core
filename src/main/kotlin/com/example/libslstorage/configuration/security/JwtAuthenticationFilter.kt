package com.example.libslstorage.configuration.security

import com.example.libslstorage.service.AccountDetailService
import com.example.libslstorage.service.TokenService
import com.example.libslstorage.util.ACCESS_TOKEN_COOKIE_NAME
import com.example.libslstorage.util.getAccessTokenCookie
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.JwtValidationException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val accountDetailService: AccountDetailService,
    private val tokenService: TokenService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val accessToken = request.getAccessTokenCookie()?.value
            if (accessToken == null) {
                logger.debug("No $ACCESS_TOKEN_COOKIE_NAME cookie present")
            } else {
                val email = tokenService.decodeToken(accessToken).subject
                val account = accountDetailService.loadUserByUsername(email)
                val authenticationToken = UsernamePasswordAuthenticationToken(
                    account,
                    null,
                    account.authorities
                )
                SecurityContextHolder.getContext().authentication = authenticationToken
            }
        } catch(e: JwtValidationException) {
            logger.debug(e.message)
        }
        filterChain.doFilter(request, response)
    }
}
