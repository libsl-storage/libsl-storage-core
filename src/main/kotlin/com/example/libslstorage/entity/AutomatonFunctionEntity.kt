package com.example.libslstorage.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "automaton_function")
class AutomatonFunctionEntity(

    @Column(nullable = false)
    var name: String,

    @ManyToOne
    @JoinColumn(name = "automaton_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var automaton: AutomatonEntity,

    @ManyToMany
    @JoinTable(
        name = "automaton_call",
        joinColumns = [ JoinColumn(name = "function_id") ],
        inverseJoinColumns = [ JoinColumn(name = "init_state_id") ]
    )
    var automatonCalls: MutableSet<AutomatonStateEntity> = mutableSetOf(),

    @OneToMany(mappedBy = "function")
    var arguments: MutableSet<AutomatonFunctionArgumentEntity> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
