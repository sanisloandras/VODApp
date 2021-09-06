package com.sanislo.vodapp.data.entity

data class VodsResponse(
    val entries: List<Entry>,
    val totalCount: Int
)