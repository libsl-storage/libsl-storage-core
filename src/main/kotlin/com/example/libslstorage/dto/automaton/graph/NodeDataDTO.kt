package com.example.libslstorage.dto.automaton.graph

data class NodeDataDTO (
    val id: String,
    val name: String?,
    val parent: String? = null
)
