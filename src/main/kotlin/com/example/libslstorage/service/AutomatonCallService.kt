package com.example.libslstorage.service

import com.example.libslstorage.entity.AutomatonCallEntity
import com.example.libslstorage.entity.AutomatonEntity
import com.example.libslstorage.entity.AutomatonFunctionEntity
import com.example.libslstorage.entity.AutomatonStateEntity
import com.example.libslstorage.repository.AutomatonCallRepository
import org.springframework.stereotype.Service

@Service
class AutomatonCallService(
    private val automatonCallRepository: AutomatonCallRepository
) {
    fun create(
        function: AutomatonFunctionEntity,
        automaton: AutomatonEntity,
        initState: AutomatonStateEntity,
    ): AutomatonCallEntity {
        return automatonCallRepository.save(
            AutomatonCallEntity(function, automaton, initState)
        )
    }
}
