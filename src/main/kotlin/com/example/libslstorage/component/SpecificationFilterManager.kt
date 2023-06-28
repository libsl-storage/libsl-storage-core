package com.example.libslstorage.component

import com.example.libslstorage.dto.specification.SpecificationFilterRequest
import com.example.libslstorage.entity.QSpecificationEntity
import com.example.libslstorage.enums.SpecificationFilter
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import org.springframework.stereotype.Component

abstract class SpecificationFilterHandler(val filter: SpecificationFilter) {
    abstract fun handle(value: String): Predicate
    fun isSupport(key: String) = filter.key == key
}

@Component
class PathFilterHandler : SpecificationFilterHandler(SpecificationFilter.PATH) {
    override fun handle(value: String): Predicate {
        return QSpecificationEntity.specificationEntity.path.like("%$value%")
    }
}

@Component
class OwnerFilterHandler : SpecificationFilterHandler(SpecificationFilter.OWNER) {
    override fun handle(value: String): Predicate {
        return QSpecificationEntity.specificationEntity.owner.name.like("%$value%")
    }
}

@Component
class NameFilterHandler : SpecificationFilterHandler(SpecificationFilter.NAME) {
    override fun handle(value: String): Predicate {
        return QSpecificationEntity.specificationEntity.name.like("%$value%")
    }
}

@Component
class LibraryFilterHandler : SpecificationFilterHandler(SpecificationFilter.LIBRARY) {
    override fun handle(value: String): Predicate {
        return QSpecificationEntity.specificationEntity.libraryName.like("%$value%")
    }
}

@Component
class VersionFilterHandler : SpecificationFilterHandler(SpecificationFilter.VERSION) {
    override fun handle(value: String): Predicate {
        return QSpecificationEntity.specificationEntity.libraryVersion.like("%$value%")
    }
}

@Component
class LanguageFilterHandler : SpecificationFilterHandler(SpecificationFilter.LANGUAGE) {
    override fun handle(value: String): Predicate {
        return QSpecificationEntity.specificationEntity.libraryLanguage.like("%$value%")
    }
}

@Component
class UrlFilterHandler : SpecificationFilterHandler(SpecificationFilter.URL) {
    override fun handle(value: String): Predicate {
        return QSpecificationEntity.specificationEntity.libraryUrl.like("%$value%")
    }
}

@Component
class TagsFilterHandler : SpecificationFilterHandler(SpecificationFilter.TAGS) {
    override fun handle(value: String): Predicate {
        val predicateBuilder = BooleanBuilder()
        value.split(',').forEach { tag ->
            predicateBuilder.and(
                QSpecificationEntity.specificationEntity.tags.any().name.eq(tag.trim())
            )
        }
        return predicateBuilder
    }
}

@Component
class LibSLVersionFilterHandler : SpecificationFilterHandler(SpecificationFilter.LIBSL) {
    override fun handle(value: String): Predicate {
        return QSpecificationEntity.specificationEntity.libslVersion.like("%$value%")
    }
}

@Component
class SpecificationFilterManager(
    private val handlers: List<SpecificationFilterHandler>
) {

    val availableHandlers = handlers.map { it.filter }

    fun handle(filter: SpecificationFilterRequest): Predicate? {
        if (filter.value.isBlank()) return null
        return handlers.find { it.isSupport(filter.key) }?.handle(filter.value)
    }
}
