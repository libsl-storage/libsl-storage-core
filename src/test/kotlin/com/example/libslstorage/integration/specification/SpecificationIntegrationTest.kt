package com.example.libslstorage.integration.specification

import com.example.libslstorage.dto.automaton.AutomatonResponse
import com.example.libslstorage.dto.specification.CreateSpecificationRequest
import com.example.libslstorage.dto.specification.SpecificationErrorResponse
import com.example.libslstorage.dto.specification.SpecificationResponse
import com.example.libslstorage.dto.tag.TagResponse
import com.example.libslstorage.entity.DirectoryEntity
import com.example.libslstorage.enums.TagGroup
import com.example.libslstorage.integration.AbstractIntegrationTest
import com.example.libslstorage.repository.DirectoryRepository
import com.example.libslstorage.service.DirectoryService
import com.example.libslstorage.util.ACCESS_TOKEN_COOKIE_NAME
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.web.reactive.function.BodyInserters

class SpecificationIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var directoryRepository: DirectoryRepository

    @Autowired
    private lateinit var directoryService: DirectoryService

    @Test
    fun `Create empty specification in root`() {
        val request = CreateSpecificationRequest("test", "", null)
        val accessToken = tokenService.createAccessToken(testUserAccount)
        webTestClient.post()
            .uri("/specification")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(SpecificationResponse::class.java)
            .value {
                assertEquals(request.name, it?.name)
                assertEquals(request.description, it?.description)
                assertEquals(directoryService.getPath(null), it?.path)
            }
    }

    @Test
    fun `Create empty specification in directory`() {
        val directory = directoryRepository.save(
            DirectoryEntity("test_dir", testUserAccount, null)
        )
        val request = CreateSpecificationRequest("test", "", directory.id!!)
        val accessToken = tokenService.createAccessToken(testUserAccount)
        webTestClient.post()
            .uri("/specification")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(SpecificationResponse::class.java)
            .value {
                assertEquals(request.name, it?.name)
                assertEquals(request.description, it?.description)
                assertEquals(directoryService.getPath(directory), it?.path)
            }
    }

    @Test
    fun `Should fail by creating specification in foreign directory`() {
        val directory = directoryRepository.save(
            DirectoryEntity("test_dir", superUserAccount, null)
        )
        val request = CreateSpecificationRequest("test", "", directory.id!!)
        val accessToken = tokenService.createAccessToken(testUserAccount)
        webTestClient.post()
            .uri("/specification")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isForbidden
    }

    @Test
    fun `Should create simple specification`() {
        val file = resourceLoader.getResource("classpath:lsl/simple.lsl")
        val request = CreateSpecificationRequest(file.file.nameWithoutExtension, "", null)
        val accessToken = tokenService.createAccessToken(testUserAccount)
        val specification = webTestClient.post()
            .uri("/specification")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(SpecificationResponse::class.java)
            .returnResult()
            .responseBody
        assertEquals(request.name, specification?.name)
        assertEquals(request.description, specification?.description)
        assertEquals("/", specification?.path)

        val multipartBodyBuilder = MultipartBodyBuilder()
        multipartBodyBuilder.part("lslFile", file)
        webTestClient.post()
            .uri("/specification/${specification?.id}/upload")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
            .exchange()
            .expectStatus()
            .isOk

        webTestClient.get()
            .uri("/specification/${specification?.id}/content")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .accept(MediaType.APPLICATION_OCTET_STREAM)
            .exchange()
            .expectBody()
            .consumeWith {
                assertThat(file.file).hasBinaryContent(it.responseBody)
            }

        webTestClient.get()
            .uri("/specification/${specification?.id}/error")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .exchange()
            .expectBody(SpecificationErrorResponse::class.java)
            .value {
                assertThat(it.errors).isEmpty()
            }

        webTestClient.get()
            .uri("/specification/${specification?.id}/automaton")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .exchange()
            .expectBody(AutomatonResponse::class.java)
            .value {
                assertThat(it.automatons).hasSize(2)
            }

        webTestClient.get()
            .uri("/specification/${specification?.id}/tag")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .exchange()
            .expectBody(TagResponse::class.java)
            .value {response ->
                assertThat(response.tagGroups).hasSize(4)
                assertThat(response.tagGroups).anyMatch { it.key == TagGroup.LANGUAGE }
                assertThat(response.tagGroups).anyMatch { it.key == TagGroup.VERSION }
                assertThat(response.tagGroups).anyMatch { it.key == TagGroup.LIBRARY }
                assertThat(response.tagGroups).anyMatch { it.key == TagGroup.URL }
            }
    }

    @Test
    fun `Should create specification with errors`() {
        val file = resourceLoader.getResource("classpath:lsl/error.lsl")
        val request = CreateSpecificationRequest(file.file.nameWithoutExtension, "", null)
        val accessToken = tokenService.createAccessToken(testUserAccount)
        val specification = webTestClient.post()
            .uri("/specification")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(SpecificationResponse::class.java)
            .returnResult()
            .responseBody
        assertEquals(request.name, specification?.name)
        assertEquals(request.description, specification?.description)
        assertEquals("/", specification?.path)

        val multipartBodyBuilder = MultipartBodyBuilder()
        multipartBodyBuilder.part("lslFile", file)
        webTestClient.post()
            .uri("/specification/${specification?.id}/upload")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
            .exchange()
            .expectStatus()
            .isOk

        webTestClient.get()
            .uri("/specification/${specification?.id}/error")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)
            .exchange()
            .expectBody(SpecificationErrorResponse::class.java)
            .value {
                assertThat(it.errors).isNotEmpty()
            }
    }
}
