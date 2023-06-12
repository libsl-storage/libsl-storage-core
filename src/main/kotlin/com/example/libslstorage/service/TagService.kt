package com.example.libslstorage.service

import com.example.libslstorage.entity.TagEntity
import com.example.libslstorage.repository.TagRepository
import org.springframework.stereotype.Service

@Service
class TagService(
    private val tagRepository: TagRepository
) {
    fun findBySpecificationId(id: Long): List<TagEntity> {
        return tagRepository.findAllBySpecificationId(id)
    }
}
