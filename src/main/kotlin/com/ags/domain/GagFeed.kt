package com.ags.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import io.quarkus.runtime.annotations.RegisterForReflection
import kotlinx.serialization.Serializable

@JacksonXmlRootElement(localName = "rss")
@RegisterForReflection
data class Rss(
        val channel: Channel,
        @get: [JacksonXmlProperty(isAttribute = true)]
        val version: String = "2.0",
)

@Serializable
@RegisterForReflection
data class Channel(
        val title: String,
        val link: String,
        val description: String,
        val pubDate: String,
        @get: [JacksonXmlProperty(isAttribute = false, namespace = "http://www.w3.org/2005/Atom",  localName = "atom:link")]
        val atomLink: AtomLink,
        val item: List<Item>
)

@Serializable
@RegisterForReflection
data class Item(
        val guid: String,
        val title: String,
        val author: String = "9gag",
        @get: [JacksonXmlCData]
        val description: String,
        val link: String,
        val pubDate: String
)

@Serializable
@RegisterForReflection
data class AtomLink(
        @get: [JacksonXmlProperty(isAttribute = true)]
        val rel: String,
        @get: [JacksonXmlProperty(isAttribute = true)]
        val href: String
)


