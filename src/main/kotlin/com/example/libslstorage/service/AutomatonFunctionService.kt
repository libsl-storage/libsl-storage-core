package com.example.libslstorage.service

import com.example.libslstorage.component.FindCallsStatementVisitor
import com.example.libslstorage.entity.AutomatonEntity
import com.example.libslstorage.entity.AutomatonFunctionEntity
import com.example.libslstorage.entity.AutomatonStateEntity
import com.example.libslstorage.repository.AutomatonFunctionRepository
import org.jetbrains.research.libsl.nodes.Automaton
import org.jetbrains.research.libsl.nodes.Function
import org.jetbrains.research.libsl.nodes.State
import org.jetbrains.research.libsl.nodes.references.AutomatonReference
import org.jetbrains.research.libsl.nodes.references.AutomatonStateReference
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AutomatonFunctionService(
    private val automatonFunctionRepository: AutomatonFunctionRepository,
    private val automatonCallService: AutomatonCallService,
    private val automatonFunctionArgumentService: AutomatonFunctionArgumentService,
    private val findCallsStatementVisitor: FindCallsStatementVisitor,
) {
    fun create(
        lslFunction: Function,
        automaton: AutomatonEntity,
        resolveAutomatonRef: (AutomatonReference) -> Pair<Automaton, AutomatonEntity>?,
        resolveAutomatonStateRef: (AutomatonStateReference, Automaton) -> Pair<State, AutomatonStateEntity>?
    ): AutomatonFunctionEntity {
        val function = automatonFunctionRepository.save(
            AutomatonFunctionEntity(lslFunction.name, automaton)
        )

        lslFunction.args.forEach {
            automatonFunctionArgumentService.create(function, it)
        }

        lslFunction.statements
            .flatMap { findCallsStatementVisitor.visit(it) }
            .forEach {
                val (targetAutomaton, targetAutomatonEntity) = resolveAutomatonRef(it.automatonRef)
                    ?: (null to null)
                if (targetAutomaton != null) {
                    val (_, initStateEntity) = resolveAutomatonStateRef(it.stateRef, targetAutomaton)
                        ?: (null to null)
                    if (initStateEntity != null)
                        automatonCallService.create(function, targetAutomatonEntity!!, initStateEntity)
                }
            }

        return function
    }
}
