package com.example.libslstorage.util

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

const val ACCESS_TOKEN_COOKIE_NAME = "accessToken"
const val REFRESH_TOKEN_COOKIE_NAME = "refreshToken"
const val AUTH_FLAG_COOKIE_NAME = "authFlag"

fun HttpServletRequest.findCookie(name: String): Cookie? =
    cookies?.find { it.name == name }

fun HttpServletResponse.addCookie(
    name: String,
    value: String?,
    maxAge: Int,
    isHttpOnly: Boolean
) {
    val cookie = Cookie(name, value)
    cookie.maxAge = maxAge
    cookie.isHttpOnly = isHttpOnly
    cookie.path = "/"
    addCookie(cookie)
}

fun HttpServletResponse.deleteCookie(request: HttpServletRequest, name: String) =
    request.findCookie(name)
        ?.apply {
            maxAge = 0
            path = "/"
            value = ""
        }?.let {
            addCookie(it)
        }
