package com.example.libslstorage.util

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest

const val ACCESS_TOKEN_COOKIE_NAME = "accessToken"
const val REFRESH_TOKEN_COOKIE_NAME = "refreshToken"

fun HttpServletRequest.getAccessTokenCookie(): Cookie? =
    cookies?.find { it.name == ACCESS_TOKEN_COOKIE_NAME }

fun HttpServletRequest.getRefreshTokenCookie(): Cookie? =
    cookies?.find { it.name == REFRESH_TOKEN_COOKIE_NAME }
