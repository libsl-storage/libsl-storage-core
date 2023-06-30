package com.example.libslstorage.dto.automaton

data class ShiftDTO(
    val startStateId: String,
    val endStateId: String,
    val functionCalls: List<Long>
)
