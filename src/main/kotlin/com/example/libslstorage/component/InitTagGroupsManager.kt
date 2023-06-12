package com.example.libslstorage.component

import com.example.libslstorage.enums.TagGroup
import com.example.libslstorage.service.TagGroupService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class InitTagGroupsManager(
    private val tagGroupService: TagGroupService,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun createTagGroups() {
        TagGroup.values().forEach { tagGroupService.createIfNotExist(it) }
    }
}
