package com.example.libslstorage.entity

import com.example.libslstorage.enums.TagGroup
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "tag_group")
class TagGroupEntity (

    @Enumerated(value = EnumType.STRING)
    var name: TagGroup,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
