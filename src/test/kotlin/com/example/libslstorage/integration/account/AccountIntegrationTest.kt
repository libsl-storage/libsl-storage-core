package com.example.libslstorage.integration.account

import com.example.libslstorage.dto.BLANK_NAME_VALIDATION_MESSAGE
import com.example.libslstorage.dto.INVALID_EMAIL_VALIDATION_MESSAGE
import com.example.libslstorage.dto.INVALID_PASSWORD_SIZE_VALIDATION_MESSAGE
import com.example.libslstorage.dto.account.AccountResponse
import com.example.libslstorage.dto.account.CreateAccountRequest
import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.exception.EMAIL_ALREADY_EXISTS_ERROR_MESSAGE
import com.example.libslstorage.integration.AbstractIntegrationTest
import com.example.libslstorage.repository.AccountRepository
import com.example.libslstorage.util.ACCESS_TOKEN_COOKIE_NAME
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder

class AccountIntegrationTest : AbstractIntegrationTest() {

    @Value("\${superuser.email}")
    private lateinit var superUserEmail: String

    @Autowired
    private lateinit var testUserAccount: AccountEntity

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @AfterEach
    fun tearDown() {
        transactionTemplate.execute {
            accountRepository.deleteAllByEmailNotIn(setOf(testUserAccount.email, superUserEmail))
        }
    }

    @Test
    fun `Should register account`() {
        val request = CreateAccountRequest("new", "new@user.new", "password")
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

    @Test
    fun `Should fail with email already taken`() {
        val request = CreateAccountRequest("new", "new@user.com", "password")

        accountRepository.save(
            AccountEntity(
                request.name,
                request.email,
                passwordEncoder.encode(request.password),
                setOf()
            )
        )

        webTestClient.post()
            .uri("/account/register")
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .jsonPath("$.message")
            .isEqualTo(EMAIL_ALREADY_EXISTS_ERROR_MESSAGE)
    }

    @Test
    fun `Should fail with validation errors`() {
        val request = CreateAccountRequest(
            "",
            "invalid",
            "quuuuuuuuuuuuuuuuuuuuuuuuuuuuuz"
        )

        webTestClient.post()
            .uri("/account/register")
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest
            .expectBody()
            .jsonPath("$.errors[?(@.code==\"NotBlank\"&&@.field==\"name\")].defaultMessage")
            .isEqualTo(BLANK_NAME_VALIDATION_MESSAGE)
            .jsonPath("$.errors[?(@.code==\"Email\"&&@.field==\"email\")].defaultMessage")
            .isEqualTo(INVALID_EMAIL_VALIDATION_MESSAGE)
            .jsonPath("$.errors[?(@.code==\"Size\"&&@.field==\"password\")].defaultMessage")
            .isEqualTo(INVALID_PASSWORD_SIZE_VALIDATION_MESSAGE)
    }

    @Test
    fun `Should return authenticated account info`() {
        val accessToken = createAccessToken(testUserAccount)
        webTestClient.get()
            .uri("/account")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(AccountResponse::class.java)
            .value {
                assertEquals(testUserAccount.id, it.id)
                assertEquals(testUserAccount.email, it.email)
                assertEquals(testUserAccount.name, it.name)
            }
    }
}
