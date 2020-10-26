package com.ags.transformer

import com.ags.domain.*

interface Function<A, B> {
    fun apply(input: A): B
}

class GagToAtom : Function<GagJson, Rss> {

    override fun apply(input: GagJson): Rss {

        val items: List<Item> = input.data.posts.map { GagPostToItem().apply(it) }

        return Rss(Channel("9GAG - NSFW - ags", "https://9gag.com/nsfw", "9GAG - NSFW", item = items))
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
        val description = when {
            input.type.equals("Animated") -> {
                val (videoUrl, contentType) =
                        if (input.images.image460sv == null) {
                            throw IllegalStateException("Can't parse ${input.images}")
                        } else if (input.images.image460sv.h265Url != null) {
                            Pair(input.images.image460sv.h265Url, "video/mp4")
                        } else if (input.images.image460sv.vp9Url != null) {
                            Pair(input.images.image460sv.vp9Url, "video/webm")
                        } else if (input.images.image460sv.vp8Url != null) {
                            Pair(input.images.image460sv.vp8Url, "video/webm")
                        } else {
                            throw IllegalStateException("Can't parse ${input.images.image460sv}")
                        }
                parseVideoTag(input.images.image700.url, videoUrl, contentType)
            }
            input.type.equals("Photo") -> {
                parserImageTag(input.images.image700.url)
            }
            else -> {
                throw IllegalStateException("Can't parse ${input.type}")
            }
        }
        return Item(guid = input.id, description = description, title = input.title, link = input.url)
    }
}


