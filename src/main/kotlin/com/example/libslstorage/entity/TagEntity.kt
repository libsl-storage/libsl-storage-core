package com.example.libslstorage.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "tag")
class TagEntity(

    @Column(nullable = false)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    val group: TagGroupEntity,

    @ManyToOne
    @JoinColumn(name = "specification_id", nullable = false)
    val specification: SpecificationEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
