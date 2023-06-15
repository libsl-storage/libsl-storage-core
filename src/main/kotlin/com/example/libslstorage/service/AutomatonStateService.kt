package com.example.libslstorage.service

import com.example.libslstorage.entity.AutomatonEntity
import com.example.libslstorage.entity.AutomatonStateEntity
import com.example.libslstorage.repository.AutomatonStateRepository
import org.jetbrains.research.libsl.nodes.State
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AutomatonStateService(
    private val automatonStateRepository: AutomatonStateRepository
) {
    fun create(
        libslState: State,
        automaton: AutomatonEntity
    ): AutomatonStateEntity {
        return automatonStateRepository.save(
            AutomatonStateEntity(libslState.name, libslState.kind, automaton)
        )
    }
}
