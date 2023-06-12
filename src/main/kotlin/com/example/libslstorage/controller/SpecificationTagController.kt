package com.example.libslstorage.controller

import com.example.libslstorage.dto.tag.TagDTO
import com.example.libslstorage.dto.tag.TagGroupDTO
import com.example.libslstorage.dto.tag.TagResponse
import com.example.libslstorage.entity.TagEntity
import com.example.libslstorage.service.TagService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/specification/{specificationId}/tag")
class SpecificationTagController(
    private val tagService: TagService
) {

    fun List<TagEntity>.toResponse(): TagResponse {
        val tagGroups = groupBy { it.group.name }
            .map { (group, tag) ->
                TagGroupDTO(group, tag.map { TagDTO(it.id!!, it.name) })
            }
        return TagResponse(tagGroups)
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
}
