package com.example.libslstorage.controller

import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.service.AccountService
import com.example.libslstorage.dto.account.AccountResponse
import com.example.libslstorage.dto.account.CreateAccountRequest
import com.example.libslstorage.dto.account.UpdateAccountPasswordRequest
import com.example.libslstorage.dto.account.UpdateAccountRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
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

    @GetMapping
    fun getCurrentAccount(@AuthenticationPrincipal account: AccountEntity): AccountResponse {
        return account.toResponse()
    }

    @PostMapping
    fun updateCurrentAccount(
        @AuthenticationPrincipal account: AccountEntity,
        @Valid @RequestBody updateRequest: UpdateAccountRequest
    ): AccountResponse {
        val updatedAccount = accountService.update(account, updateRequest)
        return updatedAccount.toResponse()
    }

    @PostMapping("/updatePassword")
    fun updatePassword(
        @AuthenticationPrincipal account: AccountEntity,
        @Valid @RequestBody updateRequest: UpdateAccountPasswordRequest
    ) {
        accountService.updatePassword(account, updateRequest)
    }
}
