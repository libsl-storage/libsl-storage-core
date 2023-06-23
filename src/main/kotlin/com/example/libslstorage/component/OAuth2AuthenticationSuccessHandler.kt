package com.example.libslstorage.component

import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.service.TokenService
import com.example.libslstorage.util.ACCESS_TOKEN_COOKIE_NAME
import com.example.libslstorage.util.AUTH_FLAG_COOKIE_NAME
import com.example.libslstorage.util.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME
import com.example.libslstorage.util.REDIRECT_URI_COOKIE_NAME
import com.example.libslstorage.util.REFRESH_TOKEN_COOKIE_NAME
import com.example.libslstorage.util.addCookie
import com.example.libslstorage.util.deleteCookie
import com.example.libslstorage.util.findCookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.net.URI
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuth2AuthenticationSuccessHandler(
    private val tokenService: TokenService
) : SimpleUrlAuthenticationSuccessHandler() {

    @Value("\${security.accessTokenMaxAge}")
    private val accessTokenMaxAge = 1200

    @Value("\${security.refreshTokenMaxAge}")
    private val refreshTokenMaxAge = 1200

    @Value("\${security.authCookieMaxAge}")
    private val authCookieMaxAge = 604800

    @Value("\${security.redirectUrls}")
    private lateinit var redirectUrls: Array<String>

    private fun isAuthorizedRedirectUri(uri: String): Boolean {
        val clientRedirectUri = URI.create(uri)
        return redirectUrls.any {
            val authorizedUri = URI.create(it)
            val isHostMatch = clientRedirectUri.host.equals(authorizedUri.host, true)
            val isPortMatch = clientRedirectUri.port == authorizedUri.port
            isHostMatch && isPortMatch
        }
    }

    fun getRedirectUri(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ): String {
        val redirectUri = request.findCookie(REDIRECT_URI_COOKIE_NAME)?.value
        if (redirectUri != null && !isAuthorizedRedirectUri(redirectUri)) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Unauthorized redirect URI"
            )
        }
        val targetUrl = redirectUri ?: defaultTargetUrl
        val accessToken = tokenService.createToken(
            authentication.principal as AccountEntity,
            accessTokenMaxAge.toLong()
        )
        val refreshToken = tokenService.createToken(
            authentication.principal as AccountEntity,
            refreshTokenMaxAge.toLong()
        )
        response.addCookie(ACCESS_TOKEN_COOKIE_NAME, accessToken, authCookieMaxAge, true)
        response.addCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, authCookieMaxAge, true)
        response.addCookie(AUTH_FLAG_COOKIE_NAME, null, authCookieMaxAge, false)
        return UriComponentsBuilder.fromUriString(targetUrl)
//            .queryParam("token", token)
            .build()
            .toUriString()
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        if (response.isCommitted) return
        val redirectUri = getRedirectUri(request, response, authentication)
        response.deleteCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        response.deleteCookie(request, REDIRECT_URI_COOKIE_NAME)
        clearAuthenticationAttributes(request)
        redirectStrategy.sendRedirect(request, response, redirectUri)
    }
}
