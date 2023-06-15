package com.example.libslstorage.configuration

import com.example.libslstorage.enums.TagGroup
import com.example.libslstorage.service.TagGroupService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InitTagGroupDataConfig(
    private val tagGroupService: TagGroupService,
) {

    @Bean
    fun tagGroupHolder() = TagGroup.values().associateWith {
        tagGroupService.createIfNotExist(it)
    }
}
