package com.example.libslstorage.dto.automaton

data class FunctionDTO(
    val id: String,
    val name: String,
    val arguments: List<FunctionArgumentDTO>,
    val automatonCalls: List<AutomatonCallDTO>
)
