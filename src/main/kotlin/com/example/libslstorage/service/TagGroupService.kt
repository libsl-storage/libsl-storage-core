package com.example.libslstorage.service

import com.example.libslstorage.entity.TagGroupEntity
import com.example.libslstorage.enums.TagGroup
import com.example.libslstorage.repository.TagGroupRepository
import org.springframework.stereotype.Service

@Service
class TagGroupService(
    private val tagGroupRepository: TagGroupRepository
) {
    fun createIfNotExist(group: TagGroup) {
        if (!tagGroupRepository.existsByName(group)) {
            val tagGroupEntity = TagGroupEntity(group)
            tagGroupRepository.save(tagGroupEntity)
        }
    }
}
