package com.example.libslstorage.dto.account

import com.example.libslstorage.dto.BLANK_NAME
import com.example.libslstorage.dto.BLANK_PASSWORD
import com.example.libslstorage.dto.INVALID_EMAIL
import com.example.libslstorage.dto.INVALID_PASSWORD_SIZE
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateAccountRequest(

    @field:NotBlank(message = BLANK_NAME)
    val name: String,

    @field:Email(message = INVALID_EMAIL)
    val email: String,

    @field:NotBlank(message = BLANK_PASSWORD)
    @field:Size(min = 8, max = 30, message = INVALID_PASSWORD_SIZE)
    val password: String
)
