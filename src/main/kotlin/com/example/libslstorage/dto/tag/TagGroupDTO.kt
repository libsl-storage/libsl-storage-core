package com.example.libslstorage.dto.tag

import com.example.libslstorage.enums.TagGroup

data class TagGroupDTO(
    val key: TagGroup,
    val tags: List<TagDTO>
)
