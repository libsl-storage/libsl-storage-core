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
@Table(name = "specification_error")
class SpecificationErrorEntity(

    @Column(nullable = false)
    val message: String,

    @Column(nullable = false)
    val startPosition: Int,

    @Column(nullable = false)
    val endPosition: Int,

    @ManyToOne
    @JoinColumn(name = "specification_id", nullable = false)
    val specification: SpecificationEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)
