package com.example.libslstorage.integration.directory

import com.example.libslstorage.dto.directory.CreateDirectoryRequest
import com.example.libslstorage.dto.directory.DirectoryResponse
import com.example.libslstorage.entity.DirectoryEntity
import com.example.libslstorage.integration.AbstractIntegrationTest
import com.example.libslstorage.repository.DirectoryRepository
import com.example.libslstorage.util.ACCESS_TOKEN_COOKIE_NAME
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class DirectoryIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var directoryRepository: DirectoryRepository

    @AfterEach
    fun tearDown() {
        transactionTemplate.execute {
            directoryRepository.deleteAll()
        }
    }

    @Test
    fun `Should create directory`() {
        val request = CreateDirectoryRequest("test")
        val accessToken = tokenService.createAccessToken(testUserAccount)
        webTestClient.post()
            .uri("/directory")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(DirectoryResponse::class.java)
            .value { response ->
                assertEquals(request.name, response.name)
                assertEquals(request.parentId, response.parentId)

                val directory = directoryRepository.findById(response.id).let {
                    Assertions.assertTrue(it.isPresent)
                    it.get()
                }
                assertEquals(request.name, directory.name)
                assertEquals(request.parentId, directory.parent?.id)
            }
    }

    @Test
    fun `Should create subdirectory`() {
        val parent = directoryRepository.save(
            DirectoryEntity("test1", testUserAccount, null)
        )
        val request = CreateDirectoryRequest("test2", parent.id)
        val accessToken = tokenService.createAccessToken(testUserAccount)
        webTestClient.post()
            .uri("/directory")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(DirectoryResponse::class.java)
            .value { response ->
                assertEquals(request.name, response.name)
                assertEquals(request.parentId, response.parentId)

                val directory = directoryRepository.findById(response.id).let {
                    Assertions.assertTrue(it.isPresent)
                    it.get()
                }
                assertEquals(request.name, directory.name)
                assertEquals(request.parentId, directory.parent?.id)
            }
    }

    @Test
    fun `Should fail by creating subdirectory in foreign directory`() {
        val parent = directoryRepository.save(
            DirectoryEntity("test1", superUserAccount, null)
        )
        val request = CreateDirectoryRequest("test2", parent.id)
        val accessToken = tokenService.createAccessToken(testUserAccount)
        webTestClient.post()
            .uri("/directory")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isForbidden
    }
}
