package com.example.libslstorage.service

import com.example.libslstorage.entity.AccountEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val authenticationManager: AuthenticationManager,
    private val tokenService: TokenService,
    private val accountDetailService: AccountDetailService,
) {

    fun authenticateByEmailPassword(email: String, password: String): AccountEntity {
        val authToken = UsernamePasswordAuthenticationToken(email, password)
        return authenticationManager.authenticate(authToken).principal as AccountEntity
    }

    fun authenticateByJwt(token: String): AccountEntity {
        val jwt = tokenService.decodeToken(token)
        return accountDetailService.loadUserByUsername(jwt.subject)
    }
}
