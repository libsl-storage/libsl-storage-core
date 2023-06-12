package com.example.libslstorage.service

import com.example.libslstorage.dto.specification.CreateSpecificationRequest
import com.example.libslstorage.dto.specification.UpdateSpecificationRequest
import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.entity.SpecificationEntity
import com.example.libslstorage.enums.UserRole
import com.example.libslstorage.exception.SpecificationAlreadyExistsException
import com.example.libslstorage.repository.SpecificationRepository
import jakarta.annotation.PostConstruct
import java.nio.file.Files
import java.nio.file.Path
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.createTempDirectory
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.notExists
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.jvm.optionals.getOrElse

@Service
@Transactional
class SpecificationService(
    private val libslService: LibSLService,
    private val directoryService: DirectoryService,
    private val specificationRepository: SpecificationRepository,
    private val specificationErrorService: SpecificationErrorService,
    private val automatonService: AutomatonService,
    private val tagService: TagService
) {

    @Value("\${libsl.tempDir}")
    private lateinit var libslTempDir: String

    private fun processFile(lslFile: Path, tempDir: Path, specification: SpecificationEntity) {
        try {
            val (libsl, library) = libslService.processFile(tempDir, lslFile)
            val errors = libsl.errorManager.errors
            if (errors.isNotEmpty()) {
                specificationErrorService.create(errors, specification)
            } else {
                tagService.createMetaTags(library.metadata, specification)
                automatonService.create(library.automataReferences, specification)
            }
        } finally {
            tempDir.toFile().deleteRecursively()
        }
    }

    @PostConstruct
    fun init() {
        val tempDirPath = Path(libslTempDir)
        if (tempDirPath.exists() && !tempDirPath.isDirectory()) Files.delete(tempDirPath)
        if (tempDirPath.notExists()) tempDirPath.createDirectory()
    }

    fun findById(id: Long): SpecificationEntity {
        return specificationRepository.findById(id).getOrElse {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "No specification by id $id")
        }
    }

    fun findByIdWithAccessCheck(id: Long, currentUser: AccountEntity): SpecificationEntity {
        val specification = findById(id)
        if (specification.owner != currentUser && currentUser.roles.all { it.name != UserRole.SUPER })
            throw AccessDeniedException("Only specification owner can update or delete it")
        return specification
    }

    fun create(
        createRequest: CreateSpecificationRequest,
        currentUser: AccountEntity
    ): SpecificationEntity {
        val directory = createRequest.directoryId
            ?.let { directoryService.findByIdWithAccessCheck(it, currentUser) }
        val path = directoryService.getPath(directory)

        val specificationsInDirectory = directory
            ?.specifications
            ?: specificationRepository.findRootSpecifications()
        if (specificationsInDirectory.any { it.name == createRequest.name })
            throw SpecificationAlreadyExistsException(createRequest.name, path)

        val specification = SpecificationEntity(
            createRequest.name,
            createRequest.description,
            path,
            directory,
            currentUser
        )
        return specificationRepository.save(specification)
    }

    fun update(
        id: Long,
        request: UpdateSpecificationRequest,
        currentUser: AccountEntity
    ): SpecificationEntity {
        val specification = findByIdWithAccessCheck(id, currentUser)
        specification.description = request.description
        return specificationRepository.save(specification)
    }

    fun updateLslFile(
        id: Long,
        file: MultipartFile,
        currentUser: AccountEntity
    ): SpecificationEntity {
        val specification = findById(id)
        val tempDir = createTempDirectory(Path(libslTempDir), "lsl-")
        try {
            val specificationsToImport = specification.directory?.specifications
                ?: specificationRepository.findRootSpecifications()
            for (spec in specificationsToImport) {
                if (spec.content == null) continue
                val lslFilePath = tempDir.resolve("${spec.name}.lsl")
                lslFilePath.writeText(spec.content!!)
            }

            val lslFilePath = tempDir.resolve("${specification.name}.lsl")
            file.transferTo(lslFilePath)

            automatonService.delete(specification.automatons)
            specificationErrorService.delete(specification.errors)

            processFile(lslFilePath, tempDir, specification)
            specification.content = lslFilePath.readText()
        } catch (e: Throwable) {
            tempDir.toFile().deleteRecursively()
            throw e
        }

        return specificationRepository.save(specification)
    }
}
