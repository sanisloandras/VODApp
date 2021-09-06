package com.sanislo.vodapp.data.entity

data class Entry(
    val availableDate: Long,
    val categories: List<Category>,
    val contents: List<Content>,
    val credits: List<Credit>,
    val description: String,
    val id: String,
    val images: List<Image>,
    val metadata: List<Metadata>,
    val parentalRatings: List<ParentalRating>,
    val publishedDate: Long,
    val title: String,
    val type: String
)