package com.example.libslstorage.dto.account

import com.example.libslstorage.dto.BLANK_NAME_VALIDATION_MESSAGE
import com.example.libslstorage.dto.INVALID_EMAIL_VALIDATION_MESSAGE
import com.example.libslstorage.dto.INVALID_PASSWORD_SIZE_VALIDATION_MESSAGE
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateAccountRequest(

    @field:NotBlank(message = BLANK_NAME_VALIDATION_MESSAGE)
    val name: String,

    @field:Email(message = INVALID_EMAIL_VALIDATION_MESSAGE)
    val email: String,

    @field:Size(min = 8, max = 30, message = INVALID_PASSWORD_SIZE_VALIDATION_MESSAGE)
    val password: String
)
