package com.example.libslstorage.service

import com.example.libslstorage.enum.UserRole
import com.example.libslstorage.entity.RoleEntity
import com.example.libslstorage.repository.RoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RoleService(
    private val roleRepository: RoleRepository
) {

    fun findByName(role: UserRole): RoleEntity {
        return roleRepository.findByName(role)
    }

    fun createIfNotExist(role: UserRole) {
        if (!roleRepository.existsByName(role)) {
            val roleEntity = RoleEntity(role)
            roleRepository.save(roleEntity)
        }
    }
}
