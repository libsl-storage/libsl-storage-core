package com.example.libslstorage.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class GraphEdgeType(
    @JsonValue
    val key: String
) {
    SHIFT("shift"),
    LOOP("loop"),
    TRANSIT("transit"),
    CALL("call")
}
