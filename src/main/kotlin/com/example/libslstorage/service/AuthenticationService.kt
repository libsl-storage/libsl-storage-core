package com.example.libslstorage.service

import com.example.libslstorage.entity.AccountEntity
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.oauth2.jwt.BadJwtException
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val authenticationManager: AuthenticationManager,
    private val tokenService: TokenService,
    private val accountDetailService: AccountDetailService,
) {

    private val logger = LoggerFactory.getLogger(AuthenticationService::class.java)

    fun authenticateByEmailPassword(email: String, password: String): AccountEntity {
        val authToken = UsernamePasswordAuthenticationToken(email, password)
        return authenticationManager.authenticate(authToken).principal as AccountEntity
    }

    fun authenticateByJwt(token: String): AccountEntity {
        try {
            val jwt = tokenService.decodeToken(token)
            return accountDetailService.loadUserByUsername(jwt.subject)
        } catch (e: BadJwtException) {
            logger.debug("Failed to authenticate since the JWT was invalid")
            throw AuthenticationServiceException(e.message, e)
        } catch (e: JwtException) {
            throw AuthenticationServiceException(e.message, e)
        }
    }
}
