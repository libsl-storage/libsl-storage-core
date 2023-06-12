package com.example.libslstorage.dto.tag

import com.example.libslstorage.enums.TagGroup

data class CreateTagRequest(
    val key: TagGroup,
    val value: String
)
