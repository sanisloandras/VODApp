package com.sanislo.vodapp.domain.mapper

import com.sanislo.vodapp.data.entity.Entry
import com.sanislo.vodapp.domain.model.VodItem

class VodEntryMapper {
    fun map(entries: List<Entry>): List<VodItem> {
        return entries.map {
            VodItem(it.id, it.title, it.images.firstOrNull()?.url)
        }
    }
}