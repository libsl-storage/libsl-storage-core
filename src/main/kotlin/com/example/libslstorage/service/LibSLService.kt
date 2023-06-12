package com.example.libslstorage.service

import java.nio.file.Path
import org.jetbrains.research.libsl.LibSL
import org.jetbrains.research.libsl.nodes.Library
import org.springframework.stereotype.Service
import kotlin.io.path.pathString

@Service
class LibSLService {

    fun processFile(basePath: Path, lslPath: Path): Pair<LibSL, Library> {
        val libsl = LibSL(basePath.pathString)
        val library = libsl.loadByPath(lslPath)
        return libsl to library
    }
}
