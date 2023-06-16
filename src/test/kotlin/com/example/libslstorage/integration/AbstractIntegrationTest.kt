package com.example.libslstorage.integration

import com.example.libslstorage.config.TestUserConfig
import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.service.TokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.core.io.ResourceLoader
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.transaction.support.TransactionTemplate
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureWebTestClient
@Import(TestUserConfig::class)
abstract class AbstractIntegrationTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var transactionTemplate: TransactionTemplate

    @Autowired
    lateinit var tokenService: TokenService

    @Autowired
    lateinit var resourceLoader: ResourceLoader

    @Autowired
    lateinit var testUserAccount: AccountEntity

    @Autowired
    lateinit var superUserAccount: AccountEntity

    fun createAccessToken(account: AccountEntity): String =
        tokenService.createToken(account, 604800)
}