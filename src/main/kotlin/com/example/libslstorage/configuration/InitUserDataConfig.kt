package com.example.libslstorage.configuration

import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.entity.RoleEntity
import com.example.libslstorage.enums.UserRole
import com.example.libslstorage.service.AccountService
import com.example.libslstorage.service.RoleService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InitUserDataConfig(
    private val roleService: RoleService,
    private val accountService: AccountService
) {

    @Value("\${superuser.name}")
    private lateinit var name: String

    @Value("\${superuser.email}")
    private lateinit var email: String

    @Value("\${superuser.password}")
    private lateinit var password: String

    @Bean
    fun roleHolder() = UserRole.values().associateWith {
        roleService.createIfNotExist(it)
    }

    @Bean
    fun superUserAccount(roleHolder: Map<UserRole, RoleEntity>): AccountEntity {
        val commonRole = roleHolder.getValue(UserRole.COMMON)
        val superRole = roleHolder.getValue(UserRole.SUPER)
        return accountService.findByEmail(email)
            ?: accountService.create(name, email, password, setOf(commonRole, superRole))
    }
}