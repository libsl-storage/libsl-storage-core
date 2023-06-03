package com.example.libslstorage.service

import com.example.libslstorage.entity.AccountEntity
import com.example.libslstorage.repository.AccountRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AccountDetailService(
    private val accountRepository: AccountRepository
): UserDetailsService {

    override fun loadUserByUsername(email: String): AccountEntity {
        return accountRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("No account found with email $email")
    }
}
