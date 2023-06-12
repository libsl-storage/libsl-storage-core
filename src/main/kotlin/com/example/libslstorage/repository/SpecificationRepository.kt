package com.example.libslstorage.repository

import com.example.libslstorage.entity.SpecificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SpecificationRepository : JpaRepository<SpecificationEntity, Long> {

    @Query("select s from SpecificationEntity s where s.directory = null")
    fun findRootSpecifications(): List<SpecificationEntity>
}
