package com.example.libslstorage.dto.specification

data class CreateSpecificationRequest (
    val name: String,
    val description: String,
    val directoryId: Long?
)
