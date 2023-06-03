package com.example.libslstorage.service

import com.example.libslstorage.entity.AccountEntity
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.stereotype.Service
import java.time.Instant.now
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters

@Service
class TokenService(
    private val jwtEncoder: JwtEncoder,
    private val jwtDecoder: JwtDecoder
) {

    fun createToken(account: AccountEntity, maxAge: Long): String {
        val now = now()
        val scope = account.authorities.joinToString(" ") { it.authority }
        val claims = JwtClaimsSet.builder()
            .issuedAt(now)
            .expiresAt(now.plusSeconds(maxAge))
            .subject(account.email)
            .claim("scp", scope)
            .build()
        val jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build()
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    fun decodeToken(token: String): Jwt = jwtDecoder.decode(token)
}
