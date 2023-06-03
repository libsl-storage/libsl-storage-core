package com.example.libslstorage.component

import com.example.libslstorage.component.event.RolesReadyEvent
import com.example.libslstorage.enum.UserRole
import com.example.libslstorage.service.RoleService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class InitRolesManager(
    private val roleService: RoleService,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @EventListener(ApplicationReadyEvent::class)
    fun createRoles() {
        UserRole.values().forEach { roleService.createIfNotExist(it) }
        applicationEventPublisher.publishEvent(RolesReadyEvent(this))
    }
}
