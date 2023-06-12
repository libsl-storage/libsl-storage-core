package com.example.libslstorage.dto.automaton

data class ShiftDTO(
    val startStateId: Long,
    val endStateId: Long,
    val functionCalls: List<Long>
)
