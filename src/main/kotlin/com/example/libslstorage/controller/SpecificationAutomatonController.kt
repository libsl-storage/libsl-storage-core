package com.example.libslstorage.controller

import com.example.libslstorage.dto.automaton.AutomatonCallDTO
import com.example.libslstorage.dto.automaton.AutomatonDTO
import com.example.libslstorage.dto.automaton.ShiftDTO
import com.example.libslstorage.dto.automaton.StateDTO
import com.example.libslstorage.entity.AutomatonEntity
import com.example.libslstorage.dto.automaton.AutomatonResponse
import com.example.libslstorage.dto.automaton.FunctionArgumentDTO
import com.example.libslstorage.dto.automaton.FunctionDTO
import com.example.libslstorage.dto.automaton.graph.EdgeDTO
import com.example.libslstorage.dto.automaton.graph.EdgeDataDTO
import com.example.libslstorage.dto.automaton.graph.GraphDTO
import com.example.libslstorage.dto.automaton.graph.GraphInfoDTO
import com.example.libslstorage.dto.automaton.graph.GraphResponse
import com.example.libslstorage.dto.automaton.graph.NodeDTO
import com.example.libslstorage.dto.automaton.graph.NodeDataDTO
import com.example.libslstorage.service.AutomatonService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/specification/{specificationId}/automaton")
class SpecificationAutomatonController(
    private val automatonService: AutomatonService
) {

    fun List<AutomatonEntity>.toResponse(): AutomatonResponse {
        val automatons = map { automaton ->
            val states = mutableListOf<StateDTO>()
            val shifts = mutableListOf<ShiftDTO>()
            val functions = mutableListOf<FunctionDTO>()
            automaton.states.forEach { state ->
                states.add(StateDTO(state.id!!, state.name))
                state.shifts.forEach { shift ->
                    val functionCalls = shift.functions.map { function ->
                        val arguments = function.arguments.map {
                            FunctionArgumentDTO(it.id!!, it.name)
                        }
                        val automatonCalls = function.automatonCalls.map {
                            AutomatonCallDTO(it.automaton.id!!, it.initState.id!!)
                        }
                        functions.add(
                            FunctionDTO(
                                function.id!!,
                                function.name,
                                arguments,
                                automatonCalls
                            )
                        )
                        function.id!!
                    }
                    shifts.add(ShiftDTO(state.id!!, shift.endState.id!!, functionCalls))
                }

            }
            AutomatonDTO(automaton.id!!, automaton.name, automaton.type, states, shifts, functions)
        }
        return AutomatonResponse(automatons)
    }

    @Operation(
        summary = "Get specification automatons",
        description = "Get specification automatons",
    )
    @GetMapping
    fun getBySpecificationId(@PathVariable specificationId: Long): AutomatonResponse {
        return automatonService.findBySpecificationId(specificationId).toResponse()
    }

    @Operation(
        summary = "Get specification automatons as graphs",
        description = "Get specification automatons as graphs",
    )
    @GetMapping("/graph")
    fun getGraphsBySpecificationId(@PathVariable specificationId: Long): GraphResponse {
        val automatons = automatonService.findBySpecificationId(specificationId)
        val generalNodes = mutableListOf<NodeDTO>()
        val generalEdges = mutableListOf<EdgeDTO>()
        val graphs = automatons.map { automaton ->
            val nodes = mutableListOf<NodeDTO>()
            val edges = mutableListOf<EdgeDTO>()
            automaton.states.forEach() { state ->
                nodes.add(NodeDTO(NodeDataDTO(state.id!!, state.name)))
                generalNodes.add(
                    NodeDTO(
                        NodeDataDTO(state.id!!, "${automaton.name}.${state.name}")
                    )
                )

                state.shifts.forEach { shift ->
                    edges.add(
                        EdgeDTO(
                            EdgeDataDTO(shift.startState.id!!, shift.endState.id!!)
                        )
                    )
                    shift.functions.forEach { function ->
                        function.automatonCalls.forEach { call ->
                            generalEdges.add(
                                EdgeDTO(
                                    EdgeDataDTO(shift.startState.id!!, call.initState.id!!)
                                )
                            )
                            call.initState.id!!
                        }
                    }
                }

            }
            GraphInfoDTO(GraphDTO(nodes, edges), automaton.name)
        }.toMutableList()
        if (graphs.size > 1) {
            graphs.add(
                GraphInfoDTO(GraphDTO(generalNodes, generalEdges), "All", true)
            )
        }
        return GraphResponse(graphs)
    }
}
