package com.example.libslstorage.service

import com.example.libslstorage.enums.UserRole
import com.example.libslstorage.entity.RoleEntity
import com.example.libslstorage.repository.RoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RoleService(
    private val roleRepository: RoleRepository
) {
    fun createIfNotExist(role: UserRole) = roleRepository.findByName(role)
        ?: roleRepository.save(RoleEntity(role))
}
