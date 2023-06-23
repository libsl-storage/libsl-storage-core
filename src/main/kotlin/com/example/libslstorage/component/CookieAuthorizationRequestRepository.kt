package com.example.libslstorage.component

import com.example.libslstorage.util.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME
import com.example.libslstorage.util.REDIRECT_URI_COOKIE_NAME
import com.example.libslstorage.util.addCookie
import com.example.libslstorage.util.findCookie
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component

@Component
class CookieAuthorizationRequestRepository(
    private val objectMapper: ObjectMapper
) : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    @Value("\${security.oauthCookieMaxAge}")
    private val oauthCookieMaxAge = 604800

    override fun loadAuthorizationRequest(
        request: HttpServletRequest
    ): OAuth2AuthorizationRequest? {
        val oauth2AuthRequestCookie = request
            .findCookie(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            ?: return null
        return objectMapper.readValue(
            oauth2AuthRequestCookie.value,
            OAuth2AuthorizationRequest::class.java
        )
    }

    override fun removeAuthorizationRequest(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): OAuth2AuthorizationRequest? {
        return loadAuthorizationRequest(request)
    }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        val oauth2AuthRequestSerialized = objectMapper
            .writeValueAsString(authorizationRequest)
        response.addCookie(
            OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
            oauth2AuthRequestSerialized,
            oauthCookieMaxAge,
            true
        )
        val redirectUri = request.getParameter(REDIRECT_URI_COOKIE_NAME)
        if (redirectUri.isNotEmpty()) {
            response.addCookie(
                REDIRECT_URI_COOKIE_NAME,
                redirectUri,
                oauthCookieMaxAge,
                true
            )
        }
    }
}
