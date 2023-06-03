package com.example.libslstorage.component

import com.example.libslstorage.component.event.RolesReadyEvent
import com.example.libslstorage.service.AccountService
import com.example.libslstorage.dto.account.CreateAccountRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class InitSuperUserManager(
    private val accountService: AccountService
) {

    @Value("\${superuser.name}")
    private lateinit var name: String

    @Value("\${superuser.email}")
    private lateinit var email: String

    @Value("\${superuser.password}")
    private lateinit var password: String

    @EventListener(RolesReadyEvent::class)
    fun createSuperUserAccount() {
        val createRequest = CreateAccountRequest(name, email, password)
        accountService.createSuper(createRequest)
    }
}
