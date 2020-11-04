package com.ags.transformer

import com.ags.domain.*
import java.text.SimpleDateFormat
import java.util.*

interface Function<A, B> {
    fun apply(input: A): B
}

private fun parseCreationTsToRF(dateTs: Long): String {
    val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    return dateFormat.format(Date(dateTs.times(1000)))
}

class GagToAtom : Function<GagJson, Rss> {

    override fun apply(input: GagJson): Rss {

        val items: List<Item> = input.data.posts.map { GagPostToItem().apply(it) }
        val group = input.data.group!!.name
        val description = input.data.group!!.description
        return Rss(
                Channel(
                        title = "9GAG - $group - GCP",
                        link = "https://9gag.com",
                        description = description,
                        item = items,
                        atomLink = AtomLink("hub", "https://pubsubhubbub.appspot.com/"),
                        pubDate = parseCreationTsToRF(input.data.posts[0].creationTs)
                )
        )
    }
}

class GagPostToItem : Function<GagPost, Item> {

    private fun parseVideoTag(poster: String, videoUrl: String, contentType: String): String {
        return """
             <video preload="auto" poster="$poster" loop muted autoplay="autoplay" controls="controls" style="width:100%;height:auto;margin:0 auto;" autoplay>
                <source src="$videoUrl" type="$contentType">
            </video>
        """.trimIndent()
    }

    private fun parserImageTag(imageUrl: String): String {
        return """
            <img src="$imageUrl" />
        """.trimIndent()
    }

    override fun apply(input: GagPost): Item {
        val description = when (input.type) {
            "Animated" -> {
                val (videoUrl, contentType) =
                        when {
                            input.images.image460sv == null -> {
                                throw IllegalStateException("Can't parse ${input.images}")
                            }
                            input.images.image460sv!!.h265Url != null -> {
                                Pair(input.images.image460sv!!.h265Url, "video/mp4")
                            }
                            input.images.image460sv!!.vp9Url != null -> {
                                Pair(input.images.image460sv!!.vp9Url, "video/webm")
                            }
                            input.images.image460sv!!.vp8Url != null -> {
                                Pair(input.images.image460sv!!.vp8Url, "video/webm")
                            }
                            else -> {
                                throw IllegalStateException("Can't parse ${input.images.image460sv}")
                            }
                        }
                parseVideoTag(input.images.image700.url, videoUrl!!, contentType)
            }
            "Photo", "Article" -> {
                parserImageTag(input.images.image700.url)
            }
            "Video" -> {
                "Videos are not implemented yet"
            }
            else -> {
                throw IllegalStateException("Can't parse ${input.type}")
            }
        }
        return Item(guid = input.id, description = description, title = input.title, link = input.url, pubDate = parseCreationTsToRF(input.creationTs))
    }
}


