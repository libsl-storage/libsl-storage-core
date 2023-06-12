package com.example.libslstorage.repository

import com.example.libslstorage.entity.SpecificationErrorEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpecificationErrorRepository : JpaRepository<SpecificationErrorEntity, Long> {
    fun findAllBySpecificationId(specificationId: Long): List<SpecificationErrorEntity>
}
