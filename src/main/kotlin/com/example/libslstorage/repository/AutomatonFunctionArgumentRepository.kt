package com.example.libslstorage.repository

import com.example.libslstorage.entity.AutomatonFunctionArgumentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AutomatonFunctionArgumentRepository: JpaRepository<AutomatonFunctionArgumentEntity, Long>
