package com.example.libslstorage.service

import com.example.libslstorage.entity.AutomatonFunctionEntity
import com.example.libslstorage.entity.AutomatonShiftEntity
import com.example.libslstorage.entity.AutomatonStateEntity
import com.example.libslstorage.repository.AutomatonShiftRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AutomatonShiftService(
    private val automatonShiftRepository: AutomatonShiftRepository
) {
    fun create(
        start: AutomatonStateEntity,
        end: AutomatonStateEntity,
        functions: List<AutomatonFunctionEntity>
    ): AutomatonShiftEntity {
        return automatonShiftRepository.save(AutomatonShiftEntity(start, end, functions))
    }
}
