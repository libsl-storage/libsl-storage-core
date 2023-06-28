package com.example.libslstorage.service

import com.example.libslstorage.entity.AutomatonEntity
import com.example.libslstorage.entity.AutomatonStateEntity
import com.example.libslstorage.entity.SpecificationEntity
import com.example.libslstorage.repository.AutomatonRepository
import org.jetbrains.research.libsl.nodes.Automaton
import org.jetbrains.research.libsl.nodes.State
import org.jetbrains.research.libsl.nodes.references.AutomatonReference
import org.jetbrains.research.libsl.nodes.references.AutomatonStateReference
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AutomatonService(
    private val automatonRepository: AutomatonRepository,
    private val automatonStateService: AutomatonStateService,
    private val automatonShiftService: AutomatonShiftService,
    private val automatonFunctionService: AutomatonFunctionService
) {

    private fun automatonResolver(
        automatons: Map<Automaton, AutomatonEntity>
    ): (AutomatonReference) -> Pair<Automaton, AutomatonEntity>? = { ref ->
        automatons.entries
            .find { ref.isReferenceMatchWithNode(it.key) }
            ?.toPair()
    }

    private fun automatonStateResolver(
        states: Map<Automaton, Map<State, AutomatonStateEntity>>
    ): (AutomatonStateReference, Automaton) -> Pair<State, AutomatonStateEntity>? = { ref, automaton ->
        states.getValue(automaton).entries
            .find { ref.isReferenceMatchWithNode(it.key) }
            ?.toPair()
    }

    fun create(
        automatonReferences: List<AutomatonReference>,
        specification: SpecificationEntity
    ) {
        val libslAutomatons = automatonReferences.map { it.resolveOrError() }
        val automatons = libslAutomatons.associateWith {
            AutomatonEntity(it.name, it.typeReference.name, specification)
        }
        automatonRepository.saveAll(automatons.values)

        val statesByAutomaton = automatons.entries.associate { (lslAutomaton, automatonEntity) ->
            val states = lslAutomaton.states.associateWith {
                it.automaton = lslAutomaton
                automatonStateService.create(it, automatonEntity)
            }
            lslAutomaton to states
        }

        automatons.forEach { (lslAutomaton, automatonEntity) ->
            val states = statesByAutomaton.getValue(lslAutomaton)

            val functions = lslAutomaton.functions.associateWith { lslFunction ->
                automatonFunctionService.create(
                    lslFunction,
                    automatonEntity,
                    automatonResolver(automatons),
                    automatonStateResolver(statesByAutomaton)
                )
            }

            lslAutomaton.shifts.forEach { lslShift ->
                val start = states.getValue(lslShift.from)
                val end = states.getValue(lslShift.to)
                val shiftFunctions = lslShift.functions.mapNotNull { functionRef ->
                    functions.entries
                        .find { functionRef.isReferenceMatchWithNode(it.key) }
                        ?.value
                }
                automatonShiftService.create(start, end, shiftFunctions)
            }
        }
    }

    fun findBySpecificationId(specificationId: Long): List<AutomatonEntity> {
        return automatonRepository.findAllBySpecificationId(specificationId)
    }

    fun delete(automatons: Set<AutomatonEntity>) {
        automatons.forEach {
            automatonFunctionService.delete(it.functions)
        }
        automatons.forEach {
            automatonStateService.delete(it.states)
        }
        automatonRepository.deleteAll(automatons)
    }
}
