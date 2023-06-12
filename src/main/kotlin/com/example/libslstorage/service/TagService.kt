package com.example.libslstorage.service

import com.example.libslstorage.dto.tag.CreateTagRequest
import com.example.libslstorage.entity.SpecificationEntity
import com.example.libslstorage.entity.TagEntity
import com.example.libslstorage.entity.TagGroupEntity
import com.example.libslstorage.enums.TagGroup
import com.example.libslstorage.repository.TagRepository
import jakarta.annotation.PostConstruct
import org.jetbrains.research.libsl.nodes.MetaNode
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.optionals.getOrElse

@Service
class TagService(
    private val tagRepository: TagRepository,
    private val tagGroupService: TagGroupService
) {

    private lateinit var tagGroups: Map<TagGroup, TagGroupEntity>

    private fun getGroup(group: TagGroup) = tagGroups.getValue(group)

    @PostConstruct
    fun init() {
        tagGroups = tagGroupService.findAvailableTagGroups().associateBy { it.name }
    }

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
    ) {
        tagRepository.save(TagEntity(request.value, getGroup(request.key), specification))
    }

    fun createMetaTags(meta: MetaNode, specification: SpecificationEntity) {
        val tags = mutableListOf(
            TagEntity(meta.name, getGroup(TagGroup.LIBRARY), specification)
        )
        meta.libraryVersion?.let {
            tags.add(TagEntity(it, getGroup(TagGroup.VERSION), specification))
        }
        meta.language?.let {
            tags.add(TagEntity(it, getGroup(TagGroup.LANGUAGE), specification))
        }
        meta.url?.let {
            tags.add(TagEntity(it, getGroup(TagGroup.URL), specification))
        }
        tagRepository.saveAll(tags)
    }

    fun delete(id: Long) {
        val tag = findById(id)
        tagRepository.delete(tag)
    }
}
