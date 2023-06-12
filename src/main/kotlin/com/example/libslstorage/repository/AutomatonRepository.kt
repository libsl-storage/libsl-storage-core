package com.example.libslstorage.repository

import com.example.libslstorage.entity.AutomatonEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AutomatonRepository : JpaRepository<AutomatonEntity, Long> {
    fun findAllBySpecificationId(specificationId: Long): List<AutomatonEntity>
}
