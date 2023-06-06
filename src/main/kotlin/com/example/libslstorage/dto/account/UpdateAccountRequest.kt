package com.example.libslstorage.dto.account

import com.example.libslstorage.dto.BLANK_NAME
import jakarta.validation.constraints.NotBlank

data class UpdateAccountRequest(

    @field:NotBlank(message = BLANK_NAME)
    val name: String,
)
