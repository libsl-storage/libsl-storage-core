package com.example.libslstorage.controller

import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.service.AccountService
import com.example.libslstorage.dto.account.AccountResponse
import com.example.libslstorage.dto.account.CreateAccountRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/account")
class AccountController(
    private val accountService: AccountService
) {

    private fun AccountEntity.toResponse() = AccountResponse(id!!, email, name)

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody createRequest: CreateAccountRequest): AccountResponse {
        val createdAccount = accountService.createCommon(createRequest)
        return createdAccount.toResponse()
    }
}
