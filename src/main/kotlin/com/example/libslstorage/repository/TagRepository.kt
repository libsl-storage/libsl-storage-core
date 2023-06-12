package com.example.libslstorage.repository

import com.example.libslstorage.entity.TagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<TagEntity, Long> {
    fun findAllBySpecificationId(specificationId: Long): List<TagEntity>
}
