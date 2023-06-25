package com.example.libslstorage.service

import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.util.ACCESS_TOKEN_COOKIE_NAME
import com.example.libslstorage.util.AUTH_FLAG_COOKIE_NAME
import com.example.libslstorage.util.REFRESH_TOKEN_COOKIE_NAME
import com.example.libslstorage.util.addCookie
import com.example.libslstorage.util.deleteCookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val tokenService: TokenService
) {

    @Value("\${security.authCookieMaxAge}")
    private val authCookieMaxAge = 604800

    fun createAuthCookies(account: AccountEntity, response: HttpServletResponse) {
        val accessToken = tokenService.createAccessToken(account)
        val refreshToken = tokenService.createRefreshToken(account)
        response.addCookie(ACCESS_TOKEN_COOKIE_NAME, accessToken, authCookieMaxAge, true)
        response.addCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, authCookieMaxAge, true)
        response.addCookie(AUTH_FLAG_COOKIE_NAME, null, authCookieMaxAge, false)
    }

    fun deleteAuthCookies(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        response.deleteCookie(request, ACCESS_TOKEN_COOKIE_NAME)
        response.deleteCookie(request, REFRESH_TOKEN_COOKIE_NAME)
        response.deleteCookie(request, AUTH_FLAG_COOKIE_NAME)
    }
}
