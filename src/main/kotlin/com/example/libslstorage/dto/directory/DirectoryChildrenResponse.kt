package com.example.libslstorage.dto.directory

data class DirectoryChildrenResponse(
    val parentId: Long?,
    val children: List<DirectoryResponse>
)
