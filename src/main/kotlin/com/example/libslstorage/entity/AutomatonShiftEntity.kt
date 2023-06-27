package com.example.libslstorage.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "automaton_shift")
class AutomatonShiftEntity(

    @ManyToOne
    @JoinColumn(name = "start_state_id", nullable = false)
    var startState: AutomatonStateEntity,

    @ManyToOne
    @JoinColumn(name = "end_state_id", nullable = false)
    var endState: AutomatonStateEntity,

    @ManyToMany
    @JoinTable(
        name = "automaton_shift_function",
        joinColumns = [JoinColumn(name = "shift_id")],
        inverseJoinColumns = [JoinColumn(name = "function_id")]
    )
    var functions: MutableSet<AutomatonFunctionEntity> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
