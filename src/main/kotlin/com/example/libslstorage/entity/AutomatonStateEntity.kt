package com.example.libslstorage.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.jetbrains.research.libsl.nodes.StateKind

@Entity
@Table(name = "automaton_state")
class AutomatonStateEntity(

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: StateKind,

    @ManyToOne
    @JoinColumn(name = "automaton_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val automaton: AutomatonEntity,

    @OneToMany(mappedBy = "startState")
    val shifts: MutableSet<AutomatonShiftEntity> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
