package com.example.libslstorage.controller

import com.example.libslstorage.dto.filter.FilterDto
import com.example.libslstorage.dto.filter.FilterResponse
import com.example.libslstorage.dto.specification.SpecificationDTO
import com.example.libslstorage.dto.specification.SpecificationPageRequest
import com.example.libslstorage.dto.tag.TagDTO
import com.example.libslstorage.service.SpecificationFilterService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/specification/page")
class SpecificationPageController(
    private val specificationFilterService: SpecificationFilterService
) {

    @Operation(
        summary = "Get specification filters",
        description = "Get titles and keys of available specification page filters"
    )
    @GetMapping("/page/filters")
    fun getAvailableFilters(): FilterResponse {
        val filters = specificationFilterService.getAvailableFilters()
            .map { FilterDto(it.key, it.value) }
        return FilterResponse(filters)
    }

    @Operation(
        summary = "Get specification page",
        description = "Get specification page by specified number and filters"
    )
    @PostMapping("/page")
    fun pageSpecification(
        @RequestBody pageRequest: SpecificationPageRequest
    ): Page<SpecificationDTO> {
        val specifications = specificationFilterService.getPage(pageRequest)
        val page = specifications.map { spec ->
            val tags = spec.tags.map { TagDTO(it.id!!, it.name) }
            SpecificationDTO(spec.id!!, spec.name, spec.description, spec.path, tags)
        }
        return page
    }
}
