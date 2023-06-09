package com.example.libslstorage.entity

import com.example.libslstorage.enums.UserRole
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority

@Entity
@Table(name = "role")
class RoleEntity(

    @Enumerated(EnumType.STRING)
    val name: UserRole,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) : GrantedAuthority {

    @ManyToMany(mappedBy = "roles")
    lateinit var accounts: List<AccountEntity>

    override fun getAuthority() = "ROLE_" + name.name
}
