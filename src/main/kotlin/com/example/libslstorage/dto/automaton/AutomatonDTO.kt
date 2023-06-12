package com.example.libslstorage.dto.automaton

data class AutomatonDTO(
    val id: Long,
    val name: String,
    val type: String,
    val states: List<StateDTO>,
    val shifts: List<ShiftDTO>,
    val functions: List<FunctionDTO>
)
