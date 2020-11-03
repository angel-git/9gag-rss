package com.ags.domain

import io.quarkus.runtime.annotations.RegisterForReflection
import java.util.*

// all data classes here need to be `var` so it works on native mode :(
@RegisterForReflection
data class GagJson(
        var data: GagData,
)

@RegisterForReflection
data class GagData(
        var posts: Array<GagPost>,
        var group: GagGroup? = null,
)

@RegisterForReflection
data class GagPost(
        var id: String = "",
        var url: String = "",
        var title: String = "",
        var type: String = "", // Animated, Photo
        var images: GagJsonImages = GagJsonImages(image700 = GagJsonPhotoImage("")),
        var creationTs: Long = 0,
        var createdOn: Long = Date().time
)

@RegisterForReflection
data class GagJsonImages(
        var image700: GagJsonPhotoImage = GagJsonPhotoImage(""),
        var image460sv: GagJsonAnimatedImage? = null
)

@RegisterForReflection
data class GagJsonPhotoImage(
        var url: String = ""
)

@RegisterForReflection
data class GagJsonAnimatedImage(
        var vp9Url: String? = null,
        var vp8Url: String? = null,
        var h265Url: String? = null,
)

@RegisterForReflection
data class GagGroup(
        var name: String,
        var description: String,
)
