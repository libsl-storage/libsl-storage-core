package com.example.libslstorage.component

import org.jetbrains.research.libsl.nodes.ActionExpression
import org.jetbrains.research.libsl.nodes.ArrayAccess
import org.jetbrains.research.libsl.nodes.ArrayLiteral
import org.jetbrains.research.libsl.nodes.AutomatonOfFunctionArgumentInvoke
import org.jetbrains.research.libsl.nodes.BinaryOpExpression
import org.jetbrains.research.libsl.nodes.BoolLiteral
import org.jetbrains.research.libsl.nodes.CallAutomatonConstructor
import org.jetbrains.research.libsl.nodes.ConstructorArgument
import org.jetbrains.research.libsl.nodes.ExpressionVisitor
import org.jetbrains.research.libsl.nodes.FloatLiteral
import org.jetbrains.research.libsl.nodes.FunctionArgument
import org.jetbrains.research.libsl.nodes.HasAutomatonConcept
import org.jetbrains.research.libsl.nodes.IntegerLiteral
import org.jetbrains.research.libsl.nodes.NamedArgumentWithValue
import org.jetbrains.research.libsl.nodes.OldValue
import org.jetbrains.research.libsl.nodes.ProcExpression
import org.jetbrains.research.libsl.nodes.ResultVariable
import org.jetbrains.research.libsl.nodes.StringLiteral
import org.jetbrains.research.libsl.nodes.ThisAccess
import org.jetbrains.research.libsl.nodes.UnaryOpExpression
import org.jetbrains.research.libsl.nodes.Variable
import org.jetbrains.research.libsl.nodes.VariableAccess
import org.springframework.stereotype.Component

@Component
class FindCallsExpressionVisitor: ExpressionVisitor<List<CallAutomatonConstructor>>() {
    override fun visitActionExpression(node: ActionExpression): List<CallAutomatonConstructor> {
        return node.action.arguments.flatMap { visit(it) }
    }

    override fun visitArrayAccess(node: ArrayAccess): List<CallAutomatonConstructor> {
        return when (val index = node.index) {
            is ArrayLiteral -> index.value.flatMap { visit(it) }
            is CallAutomatonConstructor -> listOf(index)
            is ArrayAccess -> visit(index)
            is AutomatonOfFunctionArgumentInvoke -> emptyList()
            else -> emptyList()
        }
    }

    override fun visitArrayLiteral(node: ArrayLiteral): List<CallAutomatonConstructor> {
        return node.value.flatMap { visit(it) }
    }

    override fun visitAutomatonGetter(node: AutomatonOfFunctionArgumentInvoke): List<CallAutomatonConstructor> {
        return emptyList()
    }

    override fun visitBinaryOpExpression(node: BinaryOpExpression): List<CallAutomatonConstructor> {
        return visit(node.left) + visit(node.right)
    }

    override fun visitBool(node: BoolLiteral): List<CallAutomatonConstructor> {
        return emptyList()
    }

    override fun visitCallAutomatonConstructor(node: CallAutomatonConstructor): List<CallAutomatonConstructor> {
        return listOf(node)
    }

    override fun visitConstructorArgument(node: ConstructorArgument): List<CallAutomatonConstructor> {
        return emptyList()
    }

    override fun visitFloatNumber(node: FloatLiteral): List<CallAutomatonConstructor> {
        return emptyList()
    }

    override fun visitFunctionArgument(node: FunctionArgument): List<CallAutomatonConstructor> {
        return emptyList()
    }

    override fun visitHasAutomatonConcept(node: HasAutomatonConcept): List<CallAutomatonConstructor> {
        return emptyList()
    }

    override fun visitIntegerNumber(node: IntegerLiteral): List<CallAutomatonConstructor> {
        return emptyList()
    }

    override fun visitNamedArgumentWithValue(node: NamedArgumentWithValue): List<CallAutomatonConstructor> {
        return visit(node.value)
    }

    override fun visitOldValue(node: OldValue): List<CallAutomatonConstructor> {
        return emptyList()
    }

    override fun visitProcExpression(node: ProcExpression): List<CallAutomatonConstructor> {
        return node.procedureCall.arguments.flatMap { visit(it) }
    }

    override fun visitResultVariable(node: ResultVariable): List<CallAutomatonConstructor> {
        return emptyList()
    }

    override fun visitStringValue(node: StringLiteral): List<CallAutomatonConstructor> {
        return emptyList()
    }

    override fun visitThisAccess(node: ThisAccess): List<CallAutomatonConstructor> {
        return emptyList()
    }

    override fun visitUnaryOpExpression(node: UnaryOpExpression): List<CallAutomatonConstructor> {
        return visit(node.value)
    }

    override fun visitVariable(node: Variable): List<CallAutomatonConstructor> {
        return emptyList()
    }

    override fun visitVariableAccess(node: VariableAccess): List<CallAutomatonConstructor> {
        return emptyList()
    }
}
