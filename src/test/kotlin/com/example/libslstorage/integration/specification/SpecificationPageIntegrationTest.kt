package com.example.libslstorage.integration.specification

import com.example.libslstorage.dto.specification.SpecificationFilterRequest
import com.example.libslstorage.dto.specification.SpecificationPageRequest
import com.example.libslstorage.entity.SpecificationEntity
import com.example.libslstorage.entity.TagEntity
import com.example.libslstorage.entity.TagGroupEntity
import com.example.libslstorage.enums.TagGroup
import com.example.libslstorage.integration.AbstractIntegrationTest
import com.example.libslstorage.repository.SpecificationRepository
import com.example.libslstorage.repository.TagRepository
import com.example.libslstorage.util.ACCESS_TOKEN_COOKIE_NAME
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class SpecificationPageIntegrationTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var tagGroupHolder: Map<TagGroup, TagGroupEntity>

    @Autowired
    private lateinit var specificationRepository: SpecificationRepository

    @Autowired
    private lateinit var tagRepository: TagRepository

    @Test
    fun `Check allowed filters`() {
        webTestClient.get()
            .uri("/specification/page/filters")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, createAccessToken(testUserAccount))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.keys[?(@.key=='${TagGroup.LIBRARY.key}'&&@.title=='${TagGroup.LIBRARY.title}')]")
            .exists()
            .jsonPath("$.keys[?(@.key=='${TagGroup.LANGUAGE.key}'&&@.title=='${TagGroup.LANGUAGE.title}')]")
            .exists()
            .jsonPath("$.keys[?(@.key=='${TagGroup.VERSION.key}'&&@.title=='${TagGroup.VERSION.title}')]")
            .exists()
            .jsonPath("$.keys[?(@.key=='${TagGroup.URL.key}'&&@.title=='${TagGroup.URL.title}')]")
            .exists()
            .jsonPath("$.keys[?(@.key=='${TagGroup.OTHER.key}'&&@.title=='${TagGroup.OTHER.title}')]")
            .exists()
            .jsonPath("$.keys[?(@.key=='path'&&@.title=='Path')]")
            .exists()
            .jsonPath("$.keys[?(@.key=='owner'&&@.title=='Owner')]")
            .exists()
            .jsonPath("$.keys[?(@.key=='name'&&@.title=='Specification name')]")
            .exists()
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

        val spec1Tags = tagRepository.saveAll(
            listOf(
                TagEntity(
                    name = "lib1",
                    group = tagGroupHolder.getValue(TagGroup.LIBRARY),
                    specification = spec1
                ),
                TagEntity(
                    name = "lib2",
                    group = tagGroupHolder.getValue(TagGroup.LIBRARY),
                    specification = spec1
                )
            )
        )
        val spec2Tags = tagRepository.saveAll(
            listOf(
                TagEntity(
                    name = "lib2",
                    group = tagGroupHolder.getValue(TagGroup.LIBRARY),
                    specification = spec2
                ),
                TagEntity(
                    name = "lib0",
                    group = tagGroupHolder.getValue(TagGroup.LIBRARY),
                    specification = spec2
                ),
                TagEntity(
                    name = "lib1",
                    group = tagGroupHolder.getValue(TagGroup.LIBRARY),
                    specification = spec2
                )
            )
        )
        val spec3Tags = tagRepository.saveAll(
            listOf(
                TagEntity(
                    name = "lib1",
                    group = tagGroupHolder.getValue(TagGroup.OTHER),
                    specification = spec3
                ),
                TagEntity(
                    name = "lib2",
                    group = tagGroupHolder.getValue(TagGroup.OTHER),
                    specification = spec3
                )
            )
        )

        val request = SpecificationPageRequest(
            0,
            listOf(SpecificationFilterRequest(TagGroup.LIBRARY.key, "lib1, lib2"))
        )
        webTestClient.post()
            .uri("/specification/page")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, createAccessToken(testUserAccount))
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.content.length()")
            .isEqualTo(2)
    }

    @Test
    fun `Filter by name`() {
        val spec1 = specificationRepository.save(
            SpecificationEntity(
                name = "spec1",
                description = "",
                path = "/",
                directory = null,
                owner = testUserAccount,
            )
        )
        val spec12 = specificationRepository.save(
            SpecificationEntity(
                name = "spec12",
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

        val request = SpecificationPageRequest(
            0,
            listOf(SpecificationFilterRequest("name", "spec1"))
        )
        webTestClient.post()
            .uri("/specification/page")
            .cookie(ACCESS_TOKEN_COOKIE_NAME, createAccessToken(testUserAccount))
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.content.length()")
            .isEqualTo(2)
    }
}
