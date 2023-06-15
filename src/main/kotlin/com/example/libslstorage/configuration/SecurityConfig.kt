package com.example.libslstorage.configuration

import com.example.libslstorage.component.JwtAuthenticationEntryPoint
import com.example.libslstorage.component.JwtAuthenticationFilter
import com.example.libslstorage.service.AccountDetailService
import com.example.libslstorage.util.ACCESS_TOKEN_COOKIE_NAME
import com.example.libslstorage.util.AUTH_FLAG_COOKIE_NAME
import com.example.libslstorage.util.REFRESH_TOKEN_COOKIE_NAME
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val accountDetailService: AccountDetailService,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(jwtDecoder: JwtDecoder): AuthenticationManager {
        val daoAuthProvider = DaoAuthenticationProvider()
        daoAuthProvider.setUserDetailsService(accountDetailService)
        daoAuthProvider.setPasswordEncoder(passwordEncoder())
        return ProviderManager(daoAuthProvider)
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { config ->
            config.requestMatchers(HttpMethod.GET, "/account").authenticated()
            config.requestMatchers(HttpMethod.POST, "/account").authenticated()
            config.requestMatchers(HttpMethod.POST, "/account/updatePassword").authenticated()

            config.requestMatchers(HttpMethod.POST, "/directory").authenticated()

            config.requestMatchers(HttpMethod.POST, "/specification/**").authenticated()
            config.requestMatchers(HttpMethod.POST, "/specification/page").permitAll()
            config.requestMatchers(HttpMethod.DELETE, "/specification/**").authenticated()

            config.anyRequest().permitAll()
        }

        http.sessionManagement { customizer ->
            customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        http.addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter::class.java
        )

        http.exceptionHandling { config ->
            config.authenticationEntryPoint(jwtAuthenticationEntryPoint)
        }

        http.logout { config ->
            config.logoutUrl("/auth/logout")
            config.logoutSuccessHandler(HttpStatusReturningLogoutSuccessHandler())
            config.permitAll()
            config.deleteCookies(
                ACCESS_TOKEN_COOKIE_NAME,
                REFRESH_TOKEN_COOKIE_NAME,
                AUTH_FLAG_COOKIE_NAME
            )
        }

        http.formLogin().disable()
        http.httpBasic().disable()

        http.csrf().disable()
        http.cors()

        return http.build()
    }
}
