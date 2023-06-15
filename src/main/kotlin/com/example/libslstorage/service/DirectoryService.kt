package com.example.libslstorage.service

import com.example.libslstorage.dto.directory.CreateDirectoryRequest
import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.entity.DirectoryEntity
import com.example.libslstorage.enums.UserRole
import com.example.libslstorage.exception.DIRECTORY_ACCESS_DENIED_ERROR_MESSAGE
import com.example.libslstorage.exception.DirectoryAlreadyExistsException
import com.example.libslstorage.repository.DirectoryRepository
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.optionals.getOrElse

@Service
@Transactional
class DirectoryService(
    private val directoryRepository: DirectoryRepository
) {

    fun findById(id: Long): DirectoryEntity {
        return directoryRepository.findById(id).getOrElse {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No directory by id $id")
        }
    }

    fun findByIdWithAccessCheck(id: Long, currentUser: AccountEntity): DirectoryEntity {
        val directory = findById(id)
        if (directory.owner.id != currentUser.id && currentUser.roles.all { it.name != UserRole.SUPER })
            throw AccessDeniedException(DIRECTORY_ACCESS_DENIED_ERROR_MESSAGE)
        return directory
    }

    fun findRootDirectories(): List<DirectoryEntity> {
        return directoryRepository.findRootDirectories()
    }

    fun create(
        request: CreateDirectoryRequest,
        currentUser: AccountEntity
    ): DirectoryEntity {
        val parent = request.parentId
            ?.let { findByIdWithAccessCheck(it, currentUser) }
        if (parent != null && parent.children.any { it.name == request.name })
            throw DirectoryAlreadyExistsException(request.name, getPath(parent))
        val directory = DirectoryEntity(request.name, currentUser, parent)
        return directoryRepository.save(directory)
    }

    fun getPath(directory: DirectoryEntity?): String {
        val path = mutableListOf<String>()
        var current = directory
        while (current != null) {
            path.add(current.name)
            current = current.parent
        }
        return "/${path.reversed().joinToString("/")}"
    }
}
