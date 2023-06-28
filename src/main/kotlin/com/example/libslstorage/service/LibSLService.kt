package com.example.libslstorage.service

import com.example.libslstorage.util.LslErrorListener
import java.nio.file.Path
import org.jetbrains.research.libsl.LibSL
import org.jetbrains.research.libsl.nodes.Library
import org.springframework.stereotype.Service
import kotlin.io.path.pathString

@Service
class LibSLService {

    fun processFile(
        basePath: Path,
        lslPath: Path,
        errorListener: LslErrorListener
    ): Pair<LibSL, Library?> {
        val libsl = LibSL(basePath.pathString)
        libsl.errorListener = errorListener
        var library: Library? = null
        try {
            library = libsl.loadByPath(lslPath)
            return libsl to library
        } catch (e: Throwable) {
            errorListener.reportError(e.message ?: "Unknown error", -1, -1)
        }
        return libsl to library
    }
}
