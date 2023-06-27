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
        val shift = AutomatonShiftEntity(start, end, functions.toMutableSet())
        return automatonShiftRepository.save(shift)
    }

    fun delete(shifts: Set<AutomatonShiftEntity>) {
        shifts.forEach { it.functions = mutableSetOf() }
        automatonShiftRepository.saveAll(shifts)
        automatonShiftRepository.deleteAll(shifts)
    }
}
