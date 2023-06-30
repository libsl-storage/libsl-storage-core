package com.example.libslstorage.dto.automaton.graph

import com.example.libslstorage.enums.GraphEdgeType

data class EdgeDTO(
    val data: EdgeDataDTO,
    val classes: GraphEdgeType,
) {
    constructor(
        source: String,
        target: String,
        type: GraphEdgeType,
        label: String? = null
    ) : this(EdgeDataDTO(source, target, label), type)
}
