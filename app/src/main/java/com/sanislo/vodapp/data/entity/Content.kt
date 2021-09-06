package com.sanislo.vodapp.data.entity

data class Content(
    val duration: Int,
    val format: String,
    val geoLock: Boolean,
    val height: Int,
    val id: String,
    val language: String,
    val url: String,
    val width: Int
)