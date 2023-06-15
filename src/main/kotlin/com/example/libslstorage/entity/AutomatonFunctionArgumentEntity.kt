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
@Table(name = "automaton_function_argument")
class AutomatonFunctionArgumentEntity (

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var type: String,

    @ManyToOne
    @JoinColumn(name = "function_id", nullable = false)
    val function: AutomatonFunctionEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
