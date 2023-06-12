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

@Service
class AutomatonService(
    private val automatonRepository: AutomatonRepository,
    private val automatonStateService: AutomatonStateService,
    private val automatonShiftService: AutomatonShiftService,
    private val automatonFunctionService: AutomatonFunctionService
) {

    private fun automatonResolver(
        automatons: Map<Automaton, AutomatonEntity>
    ): (AutomatonReference) -> AutomatonEntity = { ref ->
        automatons.entries
            .first { ref.isReferenceMatchWithNode(it.key) }
            .value
    }

    private fun automatonStateResolver(
        states: Map<State, AutomatonStateEntity>
    ): (AutomatonStateReference) -> AutomatonStateEntity = { ref ->
        states.entries
            .first { ref.isReferenceMatchWithNode(it.key) }
            .value
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
                    automatonStateResolver(states)
                )
            }

            lslAutomaton.shifts.forEach { lslShift ->
                val start = states.getValue(lslShift.from)
                val end = states.getValue(lslShift.to)
                val shiftFunctions = lslShift.functions.map { functionRef ->
                    functions.entries
                        .first { functionRef.isReferenceMatchWithNode(it.key) }
                        .value
                }
                automatonShiftService.create(start, end, shiftFunctions)
            }
        }
    }

    fun findBySpecificationId(specificationId: Long): List<AutomatonEntity> {
        return automatonRepository.findAllBySpecificationId(specificationId)
    }

    fun delete(automatons: List<AutomatonEntity>) {
        automatonRepository.deleteAll(automatons)
    }
}
