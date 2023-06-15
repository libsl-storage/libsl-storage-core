package com.example.libslstorage.component

import org.jetbrains.research.libsl.nodes.Action
import org.jetbrains.research.libsl.nodes.Assignment
import org.jetbrains.research.libsl.nodes.AssignmentWithCompoundOp
import org.jetbrains.research.libsl.nodes.AssignmentWithLeftUnaryOp
import org.jetbrains.research.libsl.nodes.AssignmentWithRightUnaryOp
import org.jetbrains.research.libsl.nodes.CallAutomatonConstructor
import org.jetbrains.research.libsl.nodes.ElseStatement
import org.jetbrains.research.libsl.nodes.ExpressionStatement
import org.jetbrains.research.libsl.nodes.IfStatement
import org.jetbrains.research.libsl.nodes.Proc
import org.jetbrains.research.libsl.nodes.Statement
import org.jetbrains.research.libsl.nodes.VariableDeclaration
import org.springframework.stereotype.Component

@Component
class FindCallsStatementVisitor(
    private val findCallsExpressionVisitor: FindCallsExpressionVisitor
) {
    fun visit(node: Statement): List<CallAutomatonConstructor> {
        return when (node) {
            is Assignment -> visitAssignment(node)
            is IfStatement -> visitIfStatement(node)
            is ElseStatement -> visitElseStatement(node)
            is Action -> visitAction(node)
            is ExpressionStatement -> visitExpressionStatement(node)
            is VariableDeclaration -> visitVariableDeclaration(node)
            is AssignmentWithCompoundOp -> visitAssignmentWithCompoundOp(node)
            is AssignmentWithLeftUnaryOp -> visitAssignmentWithLeftUnaryOp(node)
            is AssignmentWithRightUnaryOp -> visitAssignmentWithRightUnaryOp(node)
            is Proc -> visitProc(node)
        }
    }

    private fun visitVariableDeclaration(
        node: VariableDeclaration
    ): List<CallAutomatonConstructor> {
        return node.variable.initialValue
            ?.let { findCallsExpressionVisitor.visit(it) }
            ?: emptyList()
    }

    private fun visitExpressionStatement(
        node: ExpressionStatement
    ): List<CallAutomatonConstructor> {
        return node.expressions.flatMap { findCallsExpressionVisitor.visit(it) }
    }

    private fun visitAction(node: Action): List<CallAutomatonConstructor> {
        return node.arguments.flatMap { findCallsExpressionVisitor.visit(it) }
    }

    private fun visitElseStatement(node: ElseStatement): List<CallAutomatonConstructor> {
        return node.statements.flatMap { visit(it) }
    }

    private fun visitIfStatement(node: IfStatement): List<CallAutomatonConstructor> {
        val ifBranchResult = node.ifStatements.flatMap { visit(it) }
        val elseBranchResult = node.elseStatements
            ?.statements
            ?.flatMap { visit(it) }
            ?: emptyList()
        return ifBranchResult + elseBranchResult
    }

    private fun visitAssignment(node: Assignment): List<CallAutomatonConstructor> {
        return findCallsExpressionVisitor.visit(node.value)
    }

    private fun visitAssignmentWithCompoundOp(
        node: AssignmentWithCompoundOp
    ): List<CallAutomatonConstructor> {
        return findCallsExpressionVisitor.visit(node.value)
    }

    private fun visitAssignmentWithLeftUnaryOp(
        node: AssignmentWithLeftUnaryOp
    ): List<CallAutomatonConstructor> {
        return findCallsExpressionVisitor.visit(node.value)
    }

    private fun visitAssignmentWithRightUnaryOp(
        node: AssignmentWithRightUnaryOp
    ): List<CallAutomatonConstructor> {
        return findCallsExpressionVisitor.visit(node.value)
    }

    private fun visitProc(node: Proc): List<CallAutomatonConstructor> {
        return node.arguments.flatMap { findCallsExpressionVisitor.visit(it) }
    }
}
