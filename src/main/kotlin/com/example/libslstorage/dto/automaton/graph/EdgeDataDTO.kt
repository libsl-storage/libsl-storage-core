package com.example.libslstorage.dto.automaton.graph

data class EdgeDataDTO (
    val source: String,
    val target: String,
    val label: String? = null
)
