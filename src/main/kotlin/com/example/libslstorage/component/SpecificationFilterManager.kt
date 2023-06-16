package com.example.libslstorage.component

import com.example.libslstorage.dto.specification.SpecificationFilterRequest
import com.example.libslstorage.entity.QSpecificationEntity
import com.example.libslstorage.entity.QTagEntity
import com.example.libslstorage.enums.TagGroup
import com.querydsl.core.types.Predicate
import com.querydsl.jpa.JPAExpressions
import org.springframework.stereotype.Component

abstract class SpecificationFilterHandler(val key: String, val title: String) {
    abstract fun handle(value: String): Predicate
    fun isSupport(key: String) = this.key == key
}

abstract class TagFilterHandler(
    private val group: TagGroup
) : SpecificationFilterHandler(group.key, group.title) {
    override fun handle(value: String): Predicate {
        val filterTags = value.split(',').map { it.trim() }
        val tag = QTagEntity.tagEntity
        val tagGroupCondition = tag.group.name.eq(group)
        val tagNameCondition = tag.name.`in`(filterTags)
        val tagCondition = tagGroupCondition.and(tagNameCondition)
        val filteredTagIds = JPAExpressions.select(tag.id).from(tag).where(tagCondition)
        return QSpecificationEntity.specificationEntity.tags.any().id.`in`(filteredTagIds)
    }
}

@Component
class PathFilterHandler : SpecificationFilterHandler("path", "Path") {
    override fun handle(value: String): Predicate {
        return QSpecificationEntity.specificationEntity.path.like("%$value%")
    }
}

@Component
class OwnerFilterHandler : SpecificationFilterHandler("owner", "Owner") {
    override fun handle(value: String): Predicate {
        return QSpecificationEntity.specificationEntity.owner.name.like("%$value%")
    }
}

@Component
class NameFilterHandler : SpecificationFilterHandler("name", "Specification name") {
    override fun handle(value: String): Predicate {
        return QSpecificationEntity.specificationEntity.name.like("%$value%")
    }
}

@Component
class LibraryTagFilterHandler : TagFilterHandler(TagGroup.LIBRARY)

@Component
class LanguageTagFilterHandler : TagFilterHandler(TagGroup.LANGUAGE)

@Component
class UrlTagFilterHandler : TagFilterHandler(TagGroup.URL)

@Component
class VersionTagFilterHandler : TagFilterHandler(TagGroup.VERSION)

@Component
class OtherTagFilterHandler : TagFilterHandler(TagGroup.OTHER)

@Component
class SpecificationFilterManager(
    private val handlers: List<SpecificationFilterHandler>
) {

    val availableHandlers = handlers.associate { it.key to it.title }

    fun handle(filter: SpecificationFilterRequest): Predicate? {
        if (filter.value.isBlank()) return null
        return handlers.find { it.isSupport(filter.key) }?.handle(filter.value)
    }
}
