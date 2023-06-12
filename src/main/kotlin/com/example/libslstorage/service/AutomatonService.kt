package com.example.libslstorage.service

import com.example.libslstorage.entity.AutomatonEntity
import com.example.libslstorage.repository.AutomatonRepository
import org.springframework.stereotype.Service

@Service
class AutomatonService(
    private val automatonRepository: AutomatonRepository
) {

    fun findBySpecificationId(specificationId: Long): List<AutomatonEntity> {
        return automatonRepository.findAllBySpecificationId(specificationId)
    }
}
