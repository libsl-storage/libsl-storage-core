package com.example.libslstorage.dto.account

import com.example.libslstorage.dto.BLANK_NAME
import com.example.libslstorage.dto.INVALID_EMAIL
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UpdateAccountRequest(

    @field:NotBlank(message = BLANK_NAME)
    val name: String,

    @field:Email(message = INVALID_EMAIL)
    val email: String,
)
