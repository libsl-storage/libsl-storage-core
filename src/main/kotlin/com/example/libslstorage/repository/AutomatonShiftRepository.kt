package com.example.libslstorage.repository

import com.example.libslstorage.entity.AutomatonShiftEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AutomatonShiftRepository: JpaRepository<AutomatonShiftEntity, Long>
