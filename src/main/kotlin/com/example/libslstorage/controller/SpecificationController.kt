package com.example.libslstorage.controller

import com.example.libslstorage.dto.specification.CreateSpecificationRequest
import com.example.libslstorage.dto.specification.SpecificationResponse
import com.example.libslstorage.dto.specification.UpdateSpecificationRequest
import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.entity.SpecificationEntity
import com.example.libslstorage.exception.SpecificationContentNotFoundException
import com.example.libslstorage.service.SpecificationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/specification")
class SpecificationController(
    private val specificationService: SpecificationService
) {

    private fun SpecificationEntity.toResponse(): SpecificationResponse =
        SpecificationResponse(id!!, name, description, path)

    @Operation(
        summary = "Get specification",
        description = "Get specification by id",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "404", description = "Specification not exists")
        ]
    )
    @GetMapping("/{id}")
    fun getSpecificationById(@PathVariable id: Long): SpecificationResponse {
        return specificationService.findById(id).toResponse()
    }

    @Operation(
        summary = "Get specification content",
        description = "Get source file content of specification",
        security = [SecurityRequirement(name = "cookieAuth")],
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "404", description = "Specification not exists"),
            ApiResponse(responseCode = "409", description = "Specification has no content")
        ]
    )
    @GetMapping("/{id}/content", produces = [APPLICATION_OCTET_STREAM_VALUE])
    fun getSpecificationContent(@PathVariable id: Long): Resource {
        val specification = specificationService.findById(id)
        if (specification.content == null)
            throw SpecificationContentNotFoundException(specification.id!!)
        return ByteArrayResource(specification.content!!.toByteArray())
    }

    @Operation(
        summary = "Create specification",
        description = "Create specification with specified name and description in specified directory",
        security = [SecurityRequirement(name = "cookieAuth")],
        responses = [
            ApiResponse(responseCode = "400", description = "Specification by specified path already exists"),
            ApiResponse(responseCode = "403", description = "Only directory owner can create specifications"),
            ApiResponse(responseCode = "404", description = "Specified parent directory not exists")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSpecification(
        @RequestBody createRequest: CreateSpecificationRequest,
        @AuthenticationPrincipal currentUser: AccountEntity
    ): SpecificationResponse {
        return specificationService.create(createRequest, currentUser).toResponse()
    }

    @Operation(
        summary = "Update specification",
        description = "Update specification description",
        security = [SecurityRequirement(name = "cookieAuth")],
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "400", description = "Directory by specified path already exists"),
            ApiResponse(responseCode = "403", description = "Only specification owner can update it"),
            ApiResponse(responseCode = "404", description = "Specification not exists")
        ]
    )
    @PostMapping("/{id}")
    fun updateSpecification(
        @PathVariable id: Long,
        @RequestBody updateRequest: UpdateSpecificationRequest,
        @AuthenticationPrincipal currentUser: AccountEntity
    ): SpecificationResponse {
        return specificationService.update(id, updateRequest, currentUser).toResponse()
    }

    @Operation(
        summary = "Upload specification file",
        description = "Upload and process specification source file",
        security = [SecurityRequirement(name = "cookieAuth")],
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "403", description = "Only specification owner can update it"),
            ApiResponse(responseCode = "404", description = "Specification not exists"),
            ApiResponse(responseCode = "500", description = "Specification processing error")
        ]
    )
    @PostMapping("/{id}/upload", consumes = [APPLICATION_OCTET_STREAM_VALUE])
    fun updateSpecificationFile(
        @PathVariable id: Long,
        @RequestBody lslFile: MultipartFile,
        @AuthenticationPrincipal currentUser: AccountEntity
    ) {
        specificationService.updateLslFile(id, lslFile, currentUser).toResponse()
    }

    @Operation(
        summary = "Delete specification",
        description = "Delete specification",
        security = [SecurityRequirement(name = "cookieAuth")],
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "403", description = "Only specification owner can delete it"),
            ApiResponse(responseCode = "404", description = "Specification not exists")
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteSpecification(
        @PathVariable id: Long,
        @AuthenticationPrincipal currentUser: AccountEntity
    ) {
        specificationService.delete(id, currentUser)
    }
}
