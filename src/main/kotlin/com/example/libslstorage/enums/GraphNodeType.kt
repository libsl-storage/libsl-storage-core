package com.example.libslstorage.enums

import com.fasterxml.jackson.annotation.JsonValue
import org.jetbrains.research.libsl.nodes.StateKind

enum class GraphNodeType(
    @JsonValue
    val key: String
) {
    INIT("initstate"),
    SIMPLE("state"),
    FINISH("finishstate"),
    AUX("aux"),
    AUTOMATON("automaton")
}

fun StateKind.toGraphNodeType() =  GraphNodeType.valueOf(this.name)
