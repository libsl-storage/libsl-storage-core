package com.example.libslstorage.integration.specification

import com.example.libslstorage.dto.specification.SpecificationFilterRequest
import com.example.libslstorage.dto.specification.SpecificationPageRequest
import com.example.libslstorage.entity.SpecificationEntity
import com.example.libslstorage.entity.TagEntity
import com.example.libslstorage.enums.SpecificationFilter
import com.example.libslstorage.integration.AbstractIntegrationTest
import com.example.libslstorage.repository.SpecificationRepository
import com.example.libslstorage.repository.TagRepository
import com.example.libslstorage.util.ACCESS_TOKEN_COOKIE_NAME
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class SpecificationPageIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var specificationRepository: SpecificationRepository

    @Autowired
    private lateinit var tagRepository: TagRepository

    @Test
    fun `Check allowed filters`() {
        val bodyContentSpec = webTestClient.get()
            .uri("/specification/page/filters")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, tokenService.createAccessToken(testUserAccount))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
        SpecificationFilter.values().forEach { filter ->
            bodyContentSpec.jsonPath("$.keys[?(@.key=='${filter.key}'&&@.title=='${filter.title}')]")
                .exists()
        }
    }

    @Test
    fun `Filter by library tag`() {
        val spec1 = specificationRepository.save(
            SpecificationEntity(
                name = "spec1",
                description = "",
                path = "/",
                directory = null,
                owner = testUserAccount,
            )
        )
        val spec2 = specificationRepository.save(
            SpecificationEntity(
                name = "spec2",
                description = "",
                path = "/",
                directory = null,
                owner = testUserAccount,
            )
        )
        val spec3 = specificationRepository.save(
            SpecificationEntity(
                name = "spec3",
                description = "",
                path = "/",
                directory = null,
                owner = testUserAccount,
            )
        )

        tagRepository.saveAll(
            listOf(
                TagEntity(
                    name = "lib1",
                    specification = spec1
                ),
                TagEntity(
                    name = "lib2",
                    specification = spec1
                )
            )
        )
        tagRepository.saveAll(
            listOf(
                TagEntity(
                    name = "lib2",
                    specification = spec2
                ),
                TagEntity(
                    name = "lib0",
                    specification = spec2
                ),
                TagEntity(
                    name = "lib1",
                    specification = spec2
                )
            )
        )
        tagRepository.saveAll(
            listOf(
                TagEntity(
                    name = "lib1",
                    specification = spec3
                ),
                TagEntity(
                    name = "lib3",
                    specification = spec3
                )
            )
        )

        val request = SpecificationPageRequest(
            0,
            listOf(SpecificationFilterRequest(SpecificationFilter.TAGS.key, "lib1, lib2"))
        )
        webTestClient.post()
            .uri("/specification/page")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, tokenService.createAccessToken(testUserAccount))
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.content.length()")
            .isEqualTo(2)
            .jsonPath("$.content[?(@.name=='spec1')]")
            .exists()
            .jsonPath("$.content[?(@.name=='spec2')]")
            .exists()
    }

    @Test
    fun `Filter by name`() {
        specificationRepository.save(
            SpecificationEntity(
                name = "spec1",
                description = "",
                path = "/",
                directory = null,
                owner = testUserAccount,
            )
        )
        specificationRepository.save(
            SpecificationEntity(
                name = "spec12",
                description = "",
                path = "/",
                directory = null,
                owner = testUserAccount,
            )
        )
        specificationRepository.save(
            SpecificationEntity(
                name = "spec3",
                description = "",
                path = "/",
                directory = null,
                owner = testUserAccount,
            )
        )

        val request = SpecificationPageRequest(
            0,
            listOf(SpecificationFilterRequest("name", "spec1"))
        )
        webTestClient.post()
            .uri("/specification/page")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, tokenService.createAccessToken(testUserAccount))
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.content.length()")
            .isEqualTo(2)
            .jsonPath("$.content[?(@.name=='spec1')]")
            .exists()
            .jsonPath("$.content[?(@.name=='spec12')]")
            .exists()
    }
}
