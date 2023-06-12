package com.example.libslstorage.controller

import com.example.libslstorage.dto.specification.SpecificationErrorDTO
import com.example.libslstorage.dto.specification.SpecificationErrorResponse
import com.example.libslstorage.entity.SpecificationErrorEntity
import com.example.libslstorage.service.SpecificationErrorService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/specification/{specificationId}/error")
class SpecificationErrorController(
    private val specificationErrorService: SpecificationErrorService,
) {

    fun List<SpecificationErrorEntity>.toResponse(): SpecificationErrorResponse {
        val errors = map {
            SpecificationErrorDTO(it.message, it.startPosition, it.endPosition)
        }
        return SpecificationErrorResponse(errors)
    }

    @Operation(
        summary = "Get specification errors",
        description = "Get specification errors due file processing",
        security = [SecurityRequirement(name = "cookieAuth")],
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "404", description = "Specification not exists")
        ]
    )
    @GetMapping
    fun getBySpecificationId(
        @PathVariable specificationId: Long
    ): SpecificationErrorResponse {
        return specificationErrorService.findBySpecificationId(specificationId)
            .toResponse()
    }
}
