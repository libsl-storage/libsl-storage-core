package com.example.libslstorage.dto.account

import com.example.libslstorage.dto.BLANK_NAME_VALIDATION_MESSAGE
import jakarta.validation.constraints.NotBlank

data class UpdateAccountRequest(

    @field:NotBlank(message = BLANK_NAME_VALIDATION_MESSAGE)
    val name: String,
)
