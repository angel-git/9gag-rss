package com.ags

import com.ags.domain.*
import com.ags.domain.donglespace.*
import com.ags.transformer.dongle.DongleToAtom
import com.ags.transformer.ninegag.GagToAtom
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class AtomTest {

    @Test
    fun `should transform 9gag data classes`() {

        val gagJson = GagJson(
            GagData(
                posts = arrayOf(
                    GagPost(
                        "id1",
                        "url1",
                        "title1",
                        "Animated",
                        creationTs = 1604378061,
                        images = GagJsonImages(
                            image460sv = GagJsonAnimatedImage("vp9url"),
                            image700 = GagJsonPhotoImage("url1")
                        )
                    ),
                    GagPost(
                        "id2",
                        "url2",
                        "title2",
                        "Photo",
                        creationTs = 1604376133,
                        images = GagJsonImages(image460sv = null, image700 = GagJsonPhotoImage("url2"))
                    ),
                    GagPost(
                        "id3",
                        "url3",
                        "title3",
                        "Video",
                        creationTs = 1604376133,
                        video = GagVideo(id = "idVideo", source = "YouTube")
                    ),
                ),
                group = GagGroup("comic", "description comic")
            )
        )
        val feed = GagToAtom().apply(gagJson)
        Assertions.assertEquals(feed.channel.pubDate, "Tue, 03 Nov 2020 04:34:21 GMT")
        Assertions.assertEquals(feed.channel.item[0].guid, "id1")
        Assertions.assertEquals(feed.channel.item[0].pubDate, "Tue, 03 Nov 2020 04:34:21 GMT")
        Assertions.assertTrue(feed.channel.item[0].description.contains("vp9url"))
        Assertions.assertEquals(feed.channel.item[1].guid, "id2")
        Assertions.assertEquals(feed.channel.item[1].pubDate, "Tue, 03 Nov 2020 04:02:13 GMT")
        Assertions.assertTrue(feed.channel.item[1].description.contains("url2"))
        Assertions.assertTrue(feed.channel.item[2].description.contains("https://www.youtube.com/embed/idVideo?autoplay=0&modestbranding=1&enablejsapi=1"))
    }

    @Test
    fun `should transform data classes`() {

        val dongleJson = DonglespaceBettingJson(
            BettingData(
                Nodes(
                    arrayOf
                        (
                        Node(
                            code = "id1",
                            title = "some title",
                            post = Post(detail = "<p></p>"),
                            createdAt = Date.from(Instant.EPOCH),
                        )
                    )
                )
            )
        )
        val feed = DongleToAtom().apply(dongleJson)
        Assertions.assertEquals(feed.channel.pubDate, "Thu, 01 Jan 1970 00:00:00 GMT")
        Assertions.assertEquals(feed.channel.item[0].guid, "id1")
        Assertions.assertEquals(feed.channel.item[0].pubDate, "Thu, 01 Jan 1970 00:00:00 GMT")
        Assertions.assertTrue(feed.channel.item[0].description.contains("<p></p>"))
    }

}
