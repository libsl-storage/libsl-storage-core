package com.example.libslstorage.service

import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.exception.EmailAlreadyExistsException
import com.example.libslstorage.exception.OldPasswordNotMatchException
import com.example.libslstorage.entity.RoleEntity
import com.example.libslstorage.repository.AccountRepository
import com.example.libslstorage.dto.account.CreateAccountRequest
import com.example.libslstorage.dto.account.UpdateAccountPasswordRequest
import com.example.libslstorage.dto.account.UpdateAccountRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AccountService(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun findByEmail(email: String): AccountEntity? {
        return accountRepository.findByEmail(email)
    }

    fun create(
        name: String,
        email: String,
        password: String,
        roles: Set<RoleEntity>
    ): AccountEntity {
        accountRepository.findByEmail(email)
            ?.let { throw EmailAlreadyExistsException(email) }
        val encodedPassword = passwordEncoder.encode(password)
        val account = AccountEntity(name, email, encodedPassword, roles)
        return accountRepository.save(account)
    }

    fun create(createRequest: CreateAccountRequest, roles: Set<RoleEntity>) =
        create(createRequest.name, createRequest.email, createRequest.password, roles)

    fun update(
        account: AccountEntity,
        updateRequest: UpdateAccountRequest
    ): AccountEntity {
        account.name = updateRequest.name
        return accountRepository.save(account)
    }

    fun updatePassword(account: AccountEntity, updateRequest: UpdateAccountPasswordRequest) {
        if (!passwordEncoder.matches(updateRequest.oldPassword, account.password))
            throw OldPasswordNotMatchException()
        account.password = passwordEncoder.encode(updateRequest.newPassword)
        accountRepository.save(account)
    }
}
