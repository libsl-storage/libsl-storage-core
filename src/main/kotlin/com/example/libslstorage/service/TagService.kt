package com.example.libslstorage.service

import com.example.libslstorage.dto.tag.CreateTagRequest
import com.example.libslstorage.entity.SpecificationEntity
import com.example.libslstorage.entity.TagEntity
import com.example.libslstorage.entity.TagGroupEntity
import com.example.libslstorage.enums.TagGroup
import com.example.libslstorage.repository.TagRepository
import org.jetbrains.research.libsl.nodes.MetaNode
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.optionals.getOrElse

@Service
@Transactional
class TagService(
    private val tagRepository: TagRepository,
    private val tagGroupHolder: Map<TagGroup, TagGroupEntity>
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
            tagGroupHolder.getValue(request.key),
            specification
        )
    )

    fun createMetaTags(meta: MetaNode, specification: SpecificationEntity) {
        val tags = mutableListOf(
            TagEntity(meta.name, tagGroupHolder.getValue(TagGroup.LIBRARY), specification)
        )
        meta.libraryVersion?.let {
            tags.add(TagEntity(it, tagGroupHolder.getValue(TagGroup.VERSION), specification))
        }
        meta.language?.let {
            tags.add(TagEntity(it, tagGroupHolder.getValue(TagGroup.LANGUAGE), specification))
        }
        meta.url?.let {
            tags.add(TagEntity(it, tagGroupHolder.getValue(TagGroup.URL), specification))
        }
        tagRepository.saveAll(tags)
    }

    fun delete(id: Long) {
        val tag = findById(id)
        tagRepository.delete(tag)
    }
}
