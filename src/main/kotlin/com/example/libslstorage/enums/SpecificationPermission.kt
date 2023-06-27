package com.example.libslstorage.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class SpecificationPermission {
    EDIT,
    REMOVE
}
