package com.example.libslstorage.dto.specification

import com.example.libslstorage.enums.SpecificationPermission

data class SpecificationPermissionsResponse(
    val permissions: Set<SpecificationPermission>
)
