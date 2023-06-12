package com.example.libslstorage.service

import com.example.libslstorage.component.FindCallsStatementVisitor
import com.example.libslstorage.entity.AutomatonEntity
import com.example.libslstorage.entity.AutomatonFunctionEntity
import com.example.libslstorage.entity.AutomatonStateEntity
import com.example.libslstorage.repository.AutomatonFunctionRepository
import org.jetbrains.research.libsl.nodes.Function
import org.jetbrains.research.libsl.nodes.references.AutomatonReference
import org.jetbrains.research.libsl.nodes.references.AutomatonStateReference
import org.springframework.stereotype.Service

@Service
class AutomatonFunctionService(
    private val automatonFunctionRepository: AutomatonFunctionRepository,
    private val automatonCallService: AutomatonCallService,
    private val findCallsStatementVisitor: FindCallsStatementVisitor,
) {
    fun create(
        lslFunction: Function,
        automaton: AutomatonEntity,
        resolveAutomatonRef: (AutomatonReference) -> AutomatonEntity,
        resolveAutomatonStateRef: (AutomatonStateReference) -> AutomatonStateEntity
    ): AutomatonFunctionEntity {
        val function = automatonFunctionRepository.save(
            AutomatonFunctionEntity(lslFunction.name, automaton)
        )

        lslFunction.statements
            .flatMap { findCallsStatementVisitor.visit(it) }
            .forEach {
                val targetAutomaton = resolveAutomatonRef(it.automatonRef)
                val initState = resolveAutomatonStateRef(it.stateRef)
                automatonCallService.create(function, targetAutomaton, initState)
            }

        return function
    }
}
