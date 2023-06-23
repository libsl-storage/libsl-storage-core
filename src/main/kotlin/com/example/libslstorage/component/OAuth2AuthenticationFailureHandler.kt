package com.example.libslstorage.component

import com.example.libslstorage.util.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME
import com.example.libslstorage.util.REDIRECT_URI_COOKIE_NAME
import com.example.libslstorage.util.deleteCookie
import com.example.libslstorage.util.findCookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuth2AuthenticationFailureHandler: SimpleUrlAuthenticationFailureHandler() {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        super.onAuthenticationFailure(request, response, exception)
        var redirectUri = request.findCookie(REDIRECT_URI_COOKIE_NAME)?.value ?: "/"
        redirectUri = UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("error", exception.localizedMessage)
            .build()
            .toUriString()
        response.deleteCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        response.deleteCookie(request, REDIRECT_URI_COOKIE_NAME)
        redirectStrategy.sendRedirect(request, response, redirectUri)
    }
}
