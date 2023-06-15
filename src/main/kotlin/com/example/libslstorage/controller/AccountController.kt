package com.example.libslstorage.controller

import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.service.AccountService
import com.example.libslstorage.dto.account.AccountResponse
import com.example.libslstorage.dto.account.CreateAccountRequest
import com.example.libslstorage.dto.account.UpdateAccountPasswordRequest
import com.example.libslstorage.dto.account.UpdateAccountRequest
import com.example.libslstorage.entity.RoleEntity
import com.example.libslstorage.enums.UserRole
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/account")
class AccountController(
    private val roleHolder: Map<UserRole, RoleEntity>,
    private val accountService: AccountService
) {

    private fun AccountEntity.toResponse() = AccountResponse(id!!, email, name)

    @Operation(
        summary = "Register new account",
        description = "Register account with specified name, email and password",
        responses = [
            ApiResponse(responseCode = "400", description = "Specified email already taken")
        ]
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody createRequest: CreateAccountRequest): AccountResponse {
        val commonRole = roleHolder.getValue(UserRole.COMMON)
        val createdAccount = accountService.create(createRequest, setOf(commonRole))
        return createdAccount.toResponse()
    }

    @Operation(
        summary = "Get current account data",
        description = "Get current account data",
        security = [SecurityRequirement(name = "cookieAuth")]
    )
    @GetMapping
    fun getCurrentAccount(@AuthenticationPrincipal account: AccountEntity): AccountResponse {
        return account.toResponse()
    }

    @Operation(
        summary = "Change current account data",
        description = "Change current account name",
        security = [SecurityRequirement(name = "cookieAuth")]
    )
    @PostMapping
    fun updateCurrentAccount(
        @AuthenticationPrincipal account: AccountEntity,
        @Valid @RequestBody updateRequest: UpdateAccountRequest
    ): AccountResponse {
        val updatedAccount = accountService.update(account, updateRequest)
        return updatedAccount.toResponse()
    }

    @Operation(
        summary = "Change current account data",
        description = "Change current account name",
        security = [SecurityRequirement(name = "cookieAuth")],
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(responseCode = "400", description = "Password must be between 8 and 30 characters"),
            ApiResponse(responseCode = "403", description = "Specified old password not matches")
        ]
    )
    @PostMapping("/updatePassword")
    fun updatePassword(
        @AuthenticationPrincipal account: AccountEntity,
        @Valid @RequestBody updateRequest: UpdateAccountPasswordRequest
    ) {
        accountService.updatePassword(account, updateRequest)
    }
}
