package com.example.libslstorage.repository

import com.example.libslstorage.entity.TagGroupEntity
import com.example.libslstorage.enums.TagGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagGroupRepository: JpaRepository<TagGroupEntity, TagGroup> {
    fun existsByName(group: TagGroup): Boolean
}
