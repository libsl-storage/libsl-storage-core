package com.example.libslstorage.service

import com.example.libslstorage.entity.AutomatonFunctionArgumentEntity
import com.example.libslstorage.entity.AutomatonFunctionEntity
import com.example.libslstorage.repository.AutomatonFunctionArgumentRepository
import org.jetbrains.research.libsl.nodes.FunctionArgument
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AutomatonFunctionArgumentService(
    private val automatonFunctionArgumentRepository: AutomatonFunctionArgumentRepository
) {
    fun create(
        function: AutomatonFunctionEntity,
        argument: FunctionArgument
    ): AutomatonFunctionArgumentEntity {
        return automatonFunctionArgumentRepository.save(
            AutomatonFunctionArgumentEntity(
                argument.name,
                argument.typeReference.name,
                function
            )
        )
    }
}
