package com.example.libslstorage.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table
import java.io.Serializable
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Embeddable
data class AutomatonCallId(

    @Column(name = "function_id")
    val functionId: Long,

    @Column(name = "automaton_id")
    val automatonId: Long,

    @Column(name = "init_state_id")
    val initStateId: Long,
) : Serializable

@Entity
@Table(name = "automaton_call")
class AutomatonCallEntity(

    @MapsId("function_id")
    @ManyToOne
    @JoinColumn(name = "function_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    val function: AutomatonFunctionEntity,

    @MapsId("automaton_id")
    @ManyToOne
    @JoinColumn(name = "automaton_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    val automaton: AutomatonEntity,

    @MapsId("init_state_id")
    @ManyToOne
    @JoinColumn(name = "init_state_id")
    val initState: AutomatonStateEntity
) {
    @EmbeddedId
    var id = AutomatonCallId(function.id!!, automaton.id!!, initState.id!!)
}
