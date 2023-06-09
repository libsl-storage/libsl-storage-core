package com.example.libslstorage.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class TagGroup(
    @get:JsonValue val key: String,
    val title: String
) {
    LIBRARY("libraries", "Library"),
    VERSION("versions", "Version"),
    LANGUAGE("languages", "Language"),
    URL("url", "Links"),
    OTHER("other", "Tags");

    companion object {
        @JvmStatic
        @JsonCreator
        fun fromText(str: String) = values().find { it.key == str } ?: OTHER
    }
}
