package com.example.libslstorage.repository

import com.example.libslstorage.entity.RoleEntity
import com.example.libslstorage.enums.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: JpaRepository<RoleEntity, Long> {

    fun findByName(role: UserRole): RoleEntity

    fun existsByName(role: UserRole): Boolean
}
