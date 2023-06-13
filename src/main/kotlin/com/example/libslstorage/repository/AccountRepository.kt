package com.example.libslstorage.repository

import com.example.libslstorage.entity.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<AccountEntity, Long> {

    fun findByEmail(email: String): AccountEntity?

    fun deleteAllByEmailNotLike(email: String)
}
