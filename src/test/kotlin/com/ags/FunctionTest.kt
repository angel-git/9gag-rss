package com.ags

import com.ags.domain.*
import com.ags.transformer.GagToAtom
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@QuarkusTest
class FunctionTest {

    @Test
    fun `should transform data classes`() {

        val gagJson = GagJson(GagData(posts = arrayOf(
                GagPost("id1", "url1", "title1", "Animated", images = GagJsonImages(image460sv = GagJsonAnimatedImage("vp9url"), image700 = GagJsonPhotoImage("url1"))),
                GagPost("id2", "url2", "title2", "Photo", images = GagJsonImages(image460sv = null, image700 = GagJsonPhotoImage("url2"))),
        )))
        val feed = GagToAtom().apply(gagJson)
        Assertions.assertEquals(feed.channel.item[0].guid, "id1")
        Assertions.assertEquals(feed.channel.item[0].description, "vp9url")
        Assertions.assertEquals(feed.channel.item[1].guid, "id2")
        Assertions.assertEquals(feed.channel.item[1].description, "url2")

    }

}
