package com.example.libslstorage.config

import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.entity.RoleEntity
import com.example.libslstorage.enums.UserRole
import com.example.libslstorage.service.AccountService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestUserConfig(
    private val accountService: AccountService
) {
    @Bean
    fun testUserAccount(roleHolder: Map<UserRole, RoleEntity>): AccountEntity {
        val commonRole = roleHolder.getValue(UserRole.COMMON)
        return accountService.create(
            "test",
            "test@user.com",
            "password",
            setOf(commonRole)
        )
    }
}
