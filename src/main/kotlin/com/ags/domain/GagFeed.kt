package com.ags.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import kotlinx.serialization.Serializable

@JacksonXmlRootElement(localName = "rss")
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
        val title: String,
        val author: String = "9gag",
        @get: [JacksonXmlCData]
        val description: String,
        val link: String,
)


