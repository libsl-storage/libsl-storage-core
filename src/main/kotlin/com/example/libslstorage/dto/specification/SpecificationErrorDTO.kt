package com.example.libslstorage.dto.specification

data class SpecificationErrorDTO(
    val message: String,
    val startPosition: Int,
    val endPosition: Int,
)
