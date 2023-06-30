package com.example.libslstorage.dto.automaton.graph

import com.example.libslstorage.enums.GraphNodeType

data class NodeDTO(
    val data: NodeDataDTO,
    val classes: GraphNodeType
) {
    constructor(
        id: String,
        name: String?,
        type: GraphNodeType,
        parent: String? = null
    ) : this(NodeDataDTO(id, name, parent), type)
}
