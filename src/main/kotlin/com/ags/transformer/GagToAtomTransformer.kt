package com.ags.transformer

import com.ags.domain.*

interface Function<A, B> {
    fun apply(input: A): B
}

class GagToAtom: Function<GagJson, Rss> {

    override fun apply(input: GagJson): Rss {

        val items: List<Item> = input.data.posts.map { GagPostToItem().apply(it) }

        return Rss(Channel("9GAG - NSFW", "https://9gag.com/nsfw", "9GAG - NSFW", item = items))
    }
}

class GagPostToItem: Function<GagPost, Item> {

    override fun apply(input: GagPost): Item {
        val description = when {
            input.type.equals("Animated") -> {
                input.images.image460sv!!.vp9Url
            }
            input.type.equals("Photo") -> {
                input.images.image700.url
            }
            else -> {
                throw IllegalStateException("Can't parse ${input.type}")
            }
        }
        return Item(guid = input.id, description = description)
    }
}


