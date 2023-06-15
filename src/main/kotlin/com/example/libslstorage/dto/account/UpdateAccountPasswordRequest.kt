package com.example.libslstorage.dto.account

import com.example.libslstorage.dto.INVALID_PASSWORD_SIZE_VALIDATION_MESSAGE
import jakarta.validation.constraints.Size

data class UpdateAccountPasswordRequest(

    val oldPassword: String,

    @field:Size(min = 8, max = 30, message = INVALID_PASSWORD_SIZE_VALIDATION_MESSAGE)
    val newPassword: String
)
