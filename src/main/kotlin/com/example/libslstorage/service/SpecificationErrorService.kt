package com.example.libslstorage.service

import com.example.libslstorage.entity.SpecificationErrorEntity
import com.example.libslstorage.repository.SpecificationErrorRepository
import org.springframework.stereotype.Service

@Service
class SpecificationErrorService(
    private val specificationErrorRepository: SpecificationErrorRepository
) {
    fun findBySpecificationId(specificationId: Long): List<SpecificationErrorEntity> {
        return specificationErrorRepository.findAllBySpecificationId(specificationId)
    }
}
