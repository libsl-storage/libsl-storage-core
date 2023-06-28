package com.example.libslstorage.controller

import com.example.libslstorage.dto.tag.CreateTagRequest
import com.example.libslstorage.dto.tag.TagDTO
import com.example.libslstorage.dto.tag.TagResponse
import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.entity.TagEntity
import com.example.libslstorage.service.SpecificationService
import com.example.libslstorage.service.TagService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/specification/{specificationId}/tag")
class SpecificationTagController(
    private val specificationService: SpecificationService,
    private val tagService: TagService,
) {

    fun Collection<TagEntity>.toResponse(): TagResponse {
        val tags = map { TagDTO(it.id!!, it.name) }
        return TagResponse(tags)
    }

    @Operation(
        summary = "Get specification tags",
        description = "Get specification tags",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "404", description = "Specification not exists")
        ]
    )
    @GetMapping
    fun getBySpecificationId(@PathVariable specificationId: Long): TagResponse {
        return tagService.findBySpecificationId(specificationId).toResponse()
    }

    @Operation(
        summary = "Add tag to specification",
        description = "Create tag for specification",
        security = [SecurityRequirement(name = "cookieAuth")],
        responses = [
            ApiResponse(responseCode = "403", description = "Only specification owner can update or update it"),
            ApiResponse(responseCode = "404", description = "Specification not exists"),
            ApiResponse(responseCode = "404", description = "Specification tag not exists")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @PathVariable specificationId: Long,
        @RequestBody createRequest: CreateTagRequest,
        @AuthenticationPrincipal currentUser: AccountEntity
    ): TagResponse {
        val specification = specificationService.findByIdWithAccessCheck(specificationId, currentUser)
        tagService.create(createRequest, specification)
        return specification.tags.toResponse()
    }

    @Operation(
        summary = "Delete specification tag",
        description = "Delete tag by specified id",
        security = [SecurityRequirement(name = "cookieAuth")],
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "403", description = "Only specification owner can update or update it"),
            ApiResponse(responseCode = "404", description = "Specification not exists"),
            ApiResponse(responseCode = "404", description = "Specification tag not exists")
        ]
    )
    @DeleteMapping("/{tagId}")
    fun delete(
        @PathVariable specificationId: Long,
        @PathVariable tagId: Long,
        @AuthenticationPrincipal currentUser: AccountEntity
    ): TagResponse {
        val specification = specificationService.findByIdWithAccessCheck(
            specificationId,
            currentUser
        )
        tagService.delete(tagId)
        return specification.tags.toResponse()
    }
}
