package com.example.libslstorage.controller

import com.example.libslstorage.dto.directory.CreateDirectoryRequest
import com.example.libslstorage.dto.directory.DirectoryChildrenResponse
import com.example.libslstorage.dto.directory.DirectoryResponse
import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.entity.DirectoryEntity
import com.example.libslstorage.service.DirectoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/directory")
class DirectoryController(
    private val directoryService: DirectoryService
) {

    private fun DirectoryEntity.toResponse() = DirectoryResponse(id!!, name)

    @Operation(
        summary = "Get subdirectories",
        description = "Get subdirectories in root (without parent directory)"
    )
    @GetMapping("/children")
    fun getDirectoryChildren(): DirectoryChildrenResponse {
        val directories = directoryService.findRootDirectories()
        val children = directories.map { it.toResponse() }
        return DirectoryChildrenResponse(null, children)
    }

    @Operation(
        summary = "Get subdirectories",
        description = "Get subdirectories of specified directory",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "404", description = "Specified parent directory not exists")
        ]
    )
    @GetMapping( "/children/{id}")
    fun getDirectoryChildren(@PathVariable id: Long): DirectoryChildrenResponse {
        val parent =  directoryService.findById(id)
        val children = parent.children.map { it.toResponse() }
        return DirectoryChildrenResponse(id, children)
    }

    @Operation(
        summary = "Create directory",
        description = "Create directory with specified name in specified directory",
        security = [SecurityRequirement(name = "cookieAuth")],
        responses = [
            ApiResponse(responseCode = "400", description = "Directory by specified path already exists"),
            ApiResponse(responseCode = "403", description = "Only directory owner can create subdirectories"),
            ApiResponse(responseCode = "404", description = "Specified parent directory not exists")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: CreateDirectoryRequest,
        @AuthenticationPrincipal currentUser: AccountEntity,
    ): DirectoryResponse {
        val directory = directoryService.create(request, currentUser)
        return DirectoryResponse(directory.id!!, directory.name)
    }
}
