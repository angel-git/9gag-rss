package com.ags.domain

import kotlinx.serialization.Serializable

@Serializable
data class Rss(
    val channel: Channel
)

@Serializable
data class Channel(
        val title: String,
        val link: String,
        val description: String,
        val item: List<Item>,
)

@Serializable
data class Item(
        val guid: String,
        val author: String = "noreply@9gag-rss.com",
        val description: String
)


