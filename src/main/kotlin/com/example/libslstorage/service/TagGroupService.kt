package com.example.libslstorage.service

import com.example.libslstorage.entity.TagGroupEntity
import com.example.libslstorage.enums.TagGroup
import com.example.libslstorage.repository.TagGroupRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TagGroupService(
    private val tagGroupRepository: TagGroupRepository
) {
    fun createIfNotExist(group: TagGroup) = tagGroupRepository.findByName(group)
        ?: tagGroupRepository.save(TagGroupEntity(group))
}
