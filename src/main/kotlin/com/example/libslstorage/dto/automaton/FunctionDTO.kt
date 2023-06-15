package com.example.libslstorage.dto.automaton

data class FunctionDTO(
    val id: Long,
    val name: String,
    val arguments: List<FunctionArgumentDTO>,
    val automatonCalls: List<AutomatonCallDTO>
)
