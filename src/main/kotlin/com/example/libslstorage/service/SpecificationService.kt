package com.example.libslstorage.service

import com.example.libslstorage.dto.specification.CreateSpecificationRequest
import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.entity.SpecificationEntity
import com.example.libslstorage.exception.SpecificationAlreadyExistsException
import com.example.libslstorage.repository.SpecificationRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.optionals.getOrElse

@Service
@Transactional
class SpecificationService(
    private val directoryService: DirectoryService,
    private val specificationRepository: SpecificationRepository,
) {

    fun findById(id: Long): SpecificationEntity {
        return specificationRepository.findById(id).getOrElse {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No specification by id $id")
        }
    }

    fun create(
        createRequest: CreateSpecificationRequest,
        currentUser: AccountEntity
    ): SpecificationEntity {
        val directory = createRequest.directoryId
            ?.let { directoryService.findByIdWithAccessCheck(it, currentUser) }
        val path = directoryService.getPath(directory)

        val specificationsInDirectory = directory
            ?.specifications
            ?: specificationRepository.findRootSpecifications()
        if (specificationsInDirectory.any { it.name == createRequest.name })
            throw SpecificationAlreadyExistsException(createRequest.name, path)

        val specification = SpecificationEntity(
            createRequest.name,
            createRequest.description,
            path,
            directory,
            currentUser
        )
        return specificationRepository.save(specification)
    }
}
