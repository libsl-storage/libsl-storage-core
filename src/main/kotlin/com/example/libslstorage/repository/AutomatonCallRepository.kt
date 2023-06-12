package com.example.libslstorage.repository

import com.example.libslstorage.entity.AutomatonCallEntity
import com.example.libslstorage.entity.AutomatonCallId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AutomatonCallRepository: JpaRepository<AutomatonCallEntity, AutomatonCallId>
