package com.example.libslstorage.dto.automaton

import com.example.libslstorage.enums.GraphNodeType

data class StateDTO(
    val id: String,
    val name: String,
    val type: GraphNodeType
)
