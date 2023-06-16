package com.example.libslstorage.dto.automaton.graph

import com.fasterxml.jackson.annotation.JsonProperty

data class GraphInfoDTO(
    @JsonProperty("graph_model")
    val graphModel: GraphDTO,
    val name: String,
    val isGeneral: Boolean = false
)
