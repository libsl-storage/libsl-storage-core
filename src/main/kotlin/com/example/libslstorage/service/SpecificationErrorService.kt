package com.example.libslstorage.service

import com.example.libslstorage.entity.SpecificationEntity
import com.example.libslstorage.entity.SpecificationErrorEntity
import com.example.libslstorage.repository.SpecificationErrorRepository
import org.jetbrains.research.libsl.errors.LslError
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SpecificationErrorService(
    private val specificationErrorRepository: SpecificationErrorRepository
) {

    private fun LslError.toEntity(specification: SpecificationEntity) =
        SpecificationErrorEntity(text, position.first, position.second, specification)

    fun createByLslErrors(
        lslErrors: List<LslError>,
        specification: SpecificationEntity
    ): List<SpecificationErrorEntity> {
        val errors = lslErrors.map { it.toEntity(specification) }
        return specificationErrorRepository.saveAll(errors)
    }

    fun create(
        errors: List<SpecificationErrorEntity>,
        specification: SpecificationEntity
    ): List<SpecificationErrorEntity> {
        return specificationErrorRepository.saveAll(errors)
    }

    fun findBySpecificationId(specificationId: Long): List<SpecificationErrorEntity> {
        return specificationErrorRepository.findAllBySpecificationId(specificationId)
    }

    fun delete(errors: Set<SpecificationErrorEntity>) {
        specificationErrorRepository.deleteAll(errors)
    }
}
