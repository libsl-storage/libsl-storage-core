package com.example.libslstorage.service

import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.enum.UserRole
import com.example.libslstorage.exception.EmailConflictException
import com.example.libslstorage.entity.RoleEntity
import com.example.libslstorage.repository.AccountRepository
import com.example.libslstorage.dto.account.CreateAccountRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AccountService(
    private val roleService: RoleService,
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder
) {

    private fun checkEmailAvailable(email: String) {
        accountRepository.findByEmail(email)
            ?.let { throw EmailConflictException(email) }
    }

    private fun create(createRequest: CreateAccountRequest, roles: Set<RoleEntity>): AccountEntity {
        checkEmailAvailable(createRequest.email)
        val encodedPassword = passwordEncoder.encode(createRequest.password)
        val account = AccountEntity(createRequest.name, createRequest.email, encodedPassword, roles)
        return accountRepository.save(account)
    }

    fun createCommon(createRequest: CreateAccountRequest): AccountEntity {
        val commonRole = roleService.findByName(UserRole.COMMON)
        val roles = setOf(commonRole)
        return create(createRequest, roles)
    }

    fun createSuper(createRequest: CreateAccountRequest): AccountEntity {
        val superRole = roleService.findByName(UserRole.SUPER)
        val commonRole = roleService.findByName(UserRole.COMMON)
        val roles = setOf(commonRole, superRole)
        return superRole.accounts
            .find { it.email == createRequest.email }
            ?: create(createRequest, roles)
    }
}
