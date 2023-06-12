package com.example.libslstorage.dto.specification

import com.example.libslstorage.dto.tag.TagDTO

data class SpecificationDTO(
    val id: Long,
    val name: String,
    val description: String,
    val path: String,
    val tags: List<TagDTO>
)
