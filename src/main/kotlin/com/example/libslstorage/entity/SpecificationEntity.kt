package com.example.libslstorage.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "specification",
    uniqueConstraints = [UniqueConstraint(columnNames = ["name", "directory_id"])]
)
class SpecificationEntity(

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var description: String,

    @Column(nullable = false)
    var path: String,

    @ManyToOne
    @JoinColumn(name = "directory_id")
    var directory: DirectoryEntity?,

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    var owner: AccountEntity,

    @Column(nullable = false)
    var content: String? = null,

    @OneToMany(mappedBy = "specification")
    var errors: MutableSet<SpecificationErrorEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "specification")
    var tags: MutableSet<TagEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "specification")
    var automatons: MutableSet<AutomatonEntity> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
