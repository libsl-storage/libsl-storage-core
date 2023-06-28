package com.example.libslstorage.service

import com.example.libslstorage.dto.tag.CreateTagRequest
import com.example.libslstorage.entity.SpecificationEntity
import com.example.libslstorage.entity.TagEntity
import com.example.libslstorage.repository.TagRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.optionals.getOrElse

@Service
@Transactional
class TagService(
    private val tagRepository: TagRepository
) {

    fun findById(id: Long): TagEntity {
        return tagRepository.findById(id).getOrElse {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No tag by id $id")
        }
    }

    fun findBySpecificationId(id: Long): List<TagEntity> {
        return tagRepository.findAllBySpecificationId(id)
    }

    fun create(
        request: CreateTagRequest,
        specification: SpecificationEntity
    ) = tagRepository.save(
        TagEntity(
            request.value,
            specification
        )
    )

    fun delete(id: Long) {
        val tag = findById(id)
        tagRepository.delete(tag)
    }
}
