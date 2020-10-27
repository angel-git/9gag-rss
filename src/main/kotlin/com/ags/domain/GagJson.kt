package com.ags.domain

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class GagJson(
        val data: GagData,
)

@Serializable
data class GagData(
        val posts: Array<GagPost>,
        var group: GagGroup? = null,
)

@Serializable
data class GagPost(
        val id: String = "",
        val url: String = "",
        val title: String = "",
        val type: String = "", // Animated, Photo
        val images: GagJsonImages = GagJsonImages(image700 = GagJsonPhotoImage("")),
        val createdOn: Long = Date().time
)

@Serializable
data class GagJsonImages(
        val image700: GagJsonPhotoImage = GagJsonPhotoImage(""),
        val image460sv: GagJsonAnimatedImage? = null
)

@Serializable
data class GagJsonPhotoImage(
        val url: String = ""
)

@Serializable
data class GagJsonAnimatedImage(
        val vp9Url: String? = null,
        val vp8Url: String? = null,
        val h265Url: String? = null,
)

@Serializable
data class GagGroup(
        val name: String,
        val description: String,
)
