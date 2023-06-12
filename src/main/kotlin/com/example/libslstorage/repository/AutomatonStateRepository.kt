package com.example.libslstorage.repository

import com.example.libslstorage.entity.AutomatonStateEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AutomatonStateRepository: JpaRepository<AutomatonStateEntity, Long>
