package com.example.libslstorage.enums

enum class SpecificationFilter(val key: String, val title: String) {
    NAME("name", "File name"),
    PATH("path", "Path"),
    OWNER("owner", "Owner"),
    LIBSL("libsl", "LibSL version"),
    LIBRARY("library", "Library title"),
    VERSION("version", "Library version"),
    LANGUAGE("language", "Library language"),
    URL("url", "Library URL"),
    TAGS("tags", "Tags");
}