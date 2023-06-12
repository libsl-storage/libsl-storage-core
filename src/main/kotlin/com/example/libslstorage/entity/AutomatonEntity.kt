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
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "automaton")
class AutomatonEntity(

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var type: String,

    @ManyToOne
    @JoinColumn(name = "specification_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var specification: SpecificationEntity,

    @OneToMany(mappedBy = "automaton")
    var states: List<AutomatonStateEntity> = emptyList(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
