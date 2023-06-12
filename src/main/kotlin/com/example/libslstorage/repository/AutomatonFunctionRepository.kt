package com.example.libslstorage.repository

import com.example.libslstorage.entity.AutomatonFunctionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AutomatonFunctionRepository: JpaRepository<AutomatonFunctionEntity, Long>
