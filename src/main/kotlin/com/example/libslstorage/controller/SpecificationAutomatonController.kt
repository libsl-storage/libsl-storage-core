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
import com.example.libslstorage.dto.automaton.graph.GraphDTO
import com.example.libslstorage.dto.automaton.graph.GraphInfoDTO
import com.example.libslstorage.dto.automaton.graph.GraphResponse
import com.example.libslstorage.dto.automaton.graph.NodeDTO
import com.example.libslstorage.enums.GraphEdgeType
import com.example.libslstorage.enums.GraphNodeType
import com.example.libslstorage.enums.toGraphNodeType
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
                states.add(StateDTO(state.id!!.toString(), state.name, state.type.toGraphNodeType()))
                state.shifts.forEach { shift ->
                    val functionCalls = shift.functions.map { function ->
                        val arguments = function.arguments.map {
                            FunctionArgumentDTO(it.id!!.toString(), it.name)
                        }
                        val automatonCalls = function.automatonCalls.map {
                            AutomatonCallDTO(it.id!!.toString())
                        }
                        functions.add(
                            FunctionDTO(
                                function.id!!.toString(),
                                function.name,
                                arguments,
                                automatonCalls
                            )
                        )
                        function.id!!
                    }
                    shifts.add(ShiftDTO(state.id!!.toString(), shift.endState.id!!.toString(), functionCalls))
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
        var auxIndex = 0
        val graphs = automatons.map { automaton ->
            val nodes = mutableListOf<NodeDTO>()
            val edges = mutableListOf<EdgeDTO>()
            generalNodes.add(
                NodeDTO(
                    automaton.id!!.toString(),
                    automaton.name,
                    GraphNodeType.AUTOMATON
                )
            )
            automaton.states.forEach { state ->
                nodes.add(
                    NodeDTO(
                        state.id!!.toString(),
                        state.name,
                        state.type.toGraphNodeType()
                    )
                )
                generalNodes.add(
                    NodeDTO(
                        state.id!!.toString(),
                        state.name,
                        state.type.toGraphNodeType(),
                        automaton.id!!.toString()
                    )
                )
                state.shifts.forEach { shift ->
                    val edgeType = if (shift.startState.id == shift.endState.id)
                        GraphEdgeType.LOOP
                    else
                        GraphEdgeType.SHIFT

                    if (shift.functions.isEmpty()) {
                        val edge = EdgeDTO(
                            shift.startState.id!!.toString(),
                            shift.endState.id!!.toString(),
                            edgeType
                        )
                        edges.add(edge)
                        generalEdges.add(edge)
                    } else {
                        shift.functions.groupBy { function ->
                            function.automatonCalls.map { state -> state.id!! }
                        }.forEach { (calls, functions) ->
                            val label = functions.joinToString("\n") { function ->
                                val args = function.arguments
                                    .joinToString(", ") { arg ->
                                        "${arg.name}: ${arg.type}"
                                    }
                                "${function.name}($args)"
                            }
                            edges.add(
                                EdgeDTO(
                                    shift.startState.id!!.toString(),
                                    shift.endState.id!!.toString(),
                                    edgeType,
                                    label
                                )
                            )
                            if (calls.isEmpty()) {
                                generalEdges.add(
                                    EdgeDTO(
                                        shift.startState.id!!.toString(),
                                        shift.endState.id!!.toString(),
                                        edgeType,
                                        label
                                    )
                                )
                            } else {
                                val auxNodeId = "aux${auxIndex++}"
                                generalNodes.add(NodeDTO(
                                    auxNodeId,
                                    null,
                                    GraphNodeType.AUX,
                                    automaton.id!!.toString()
                                ))
                                generalEdges.add(
                                    EdgeDTO(
                                        shift.startState.id!!.toString(),
                                        auxNodeId,
                                        GraphEdgeType.TRANSIT,
                                        label
                                    )
                                )
                                generalEdges.add(
                                    EdgeDTO(
                                        auxNodeId,
                                        shift.endState.id!!.toString(),
                                        GraphEdgeType.SHIFT
                                    )
                                )
                                calls.forEach { call ->
                                    generalEdges.add(
                                        EdgeDTO(
                                            auxNodeId,
                                            call.toString(),
                                            GraphEdgeType.CALL
                                        )
                                    )
                                }
                            }
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
