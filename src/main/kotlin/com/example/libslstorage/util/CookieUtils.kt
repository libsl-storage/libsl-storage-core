package com.example.libslstorage.util

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest

const val ACCESS_TOKEN_COOKIE_NAME = "accessToken"
const val REFRESH_TOKEN_COOKIE_NAME = "refreshToken"
const val AUTH_FLAG_COOKIE_NAME = "authFlag"

fun HttpServletRequest.getAccessTokenCookie(): Cookie? =
    cookies?.find { it.name == ACCESS_TOKEN_COOKIE_NAME }

fun HttpServletRequest.getRefreshTokenCookie(): Cookie? =
    cookies?.find { it.name == REFRESH_TOKEN_COOKIE_NAME }

fun HttpServletRequest.getAuthFlagCookie(): Cookie? =
    cookies?.find { it.name == AUTH_FLAG_COOKIE_NAME }
