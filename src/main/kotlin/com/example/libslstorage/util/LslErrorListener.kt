package com.example.libslstorage.util

import com.example.libslstorage.entity.SpecificationEntity
import com.example.libslstorage.entity.SpecificationErrorEntity
import java.util.*
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA

class LslErrorListener(val specification: SpecificationEntity) : BaseErrorListener() {

    val errors = mutableListOf<SpecificationErrorEntity>()
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        val token = offendingSymbol as? Token
        val start = token?.startIndex ?: -1
        val end = token?.stopIndex ?: -1
        reportError(msg ?: "Syntax error", start, end)
    }

    override fun reportAmbiguity(
        recognizer: Parser?,
        dfa: DFA?,
        startIndex: Int,
        stopIndex: Int,
        exact: Boolean,
        ambigAlts: BitSet?,
        configs: ATNConfigSet?
    ) {
    }

    override fun reportAttemptingFullContext(
        recognizer: Parser?,
        dfa: DFA?,
        startIndex: Int,
        stopIndex: Int,
        conflictingAlts: BitSet?,
        configs: ATNConfigSet?
    ) {
    }

    override fun reportContextSensitivity(
        recognizer: Parser?,
        dfa: DFA?,
        startIndex: Int,
        stopIndex: Int,
        prediction: Int,
        configs: ATNConfigSet?
    ) {
    }

    fun reportError(message: String, start: Int, end: Int) {
        errors.add(SpecificationErrorEntity(message, start, end, specification))
    }
}
