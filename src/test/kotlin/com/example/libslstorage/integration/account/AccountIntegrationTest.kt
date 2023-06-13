package com.example.libslstorage.integration.account

import com.example.libslstorage.dto.account.AccountResponse
import com.example.libslstorage.dto.account.CreateAccountRequest
import com.example.libslstorage.integration.AbstractIntegrationTest
import com.example.libslstorage.repository.AccountRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder

class AccountIntegrationTest : AbstractIntegrationTest() {

    @Value("\${superuser.email}")
    lateinit var superuserEmail: String

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var accountRepository: AccountRepository

    @BeforeEach
    fun init() {
        accountRepository.deleteAllByEmailNotLike(superuserEmail)
    }

    @Test
    fun `Should register account`() {
        val request = CreateAccountRequest(
            "test",
            "test@test.example",
            "password"
        )
        webTestClient.post()
            .uri("/account/register")
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(AccountResponse::class.java)
            .value { response ->
                assertEquals(request.name, response.name)
                assertEquals(request.email, response.email)

                val account = accountRepository.findById(response.id).let {
                    assertTrue(it.isPresent)
                    it.get()
                }
                assertEquals(request.name, account.name)
                assertEquals(request.email, account.email)
                assertTrue(passwordEncoder.matches(request.password, account.password))
            }

    }
}