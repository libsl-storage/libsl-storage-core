package com.example.libslstorage.repository

import com.example.libslstorage.entity.DirectoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DirectoryRepository : JpaRepository<DirectoryEntity, Long> {

    @Query("select d from DirectoryEntity d where d.parent = null")
    fun findRootDirectories(): List<DirectoryEntity>
}
