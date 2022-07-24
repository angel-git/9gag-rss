package com.ags

import com.ags.domain.Rss
import com.ags.domain.ninegag.*
import com.ags.transformer.ninegag.GagToAtom
import com.ags.transformer.XmlSerializer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class XmlSerializerTest {

    private val expected: String = """
        <rss version="2.0">
          <channel>
            <title>9GAG - comic - GCP</title>
            <link>https://9gag.com</link>
            <description>description comic</description>
            <pubDate>Tue, 03 Nov 2020 04:34:21 GMT</pubDate>
            <item>
              <guid>id1</guid>
              <title>title1</title>
              <author>9gag</author>
              <description><![CDATA[ <video preload="auto" poster="url1" loop muted autoplay="autoplay" controls="controls" style="width:100%;height:auto;margin:0 auto;" autoplay>
    <source src="vp9url" type="video/webm">
</video>]]></description>
              <link>url1</link>
              <pubDate>Tue, 03 Nov 2020 04:34:21 GMT</pubDate>
            </item>
            <item>
              <guid>id2</guid>
              <title>title2</title>
              <author>9gag</author>
              <description><![CDATA[<img src="url2" />]]></description>
              <link>url2</link>
              <pubDate>Tue, 03 Nov 2020 04:02:13 GMT</pubDate>
            </item>
            <wstxns1:link xmlns:wstxns1="http://www.w3.org/2005/Atom" rel="hub" href="https://pubsubhubbub.appspot.com/"/>
          </channel>
        </rss>"""
            .trimIndent()

    @Test
    fun `should create RSS xml compatible`() {

        val gagJson = GagJson(
                GagData(
                        posts = arrayOf(
                                GagPost("id1", "url1", "title1", "Animated", creationTs = 1604378061, images = GagJsonImages(image460sv = GagJsonAnimatedImage("vp9url"), image700 = GagJsonPhotoImage("url1"))),
                                GagPost("id2", "url2", "title2", "Photo", creationTs = 1604376133, images = GagJsonImages(image460sv = null, image700 = GagJsonPhotoImage("url2")))),
                        group = GagGroup("comic", "description comic")
                ))
        val feed: Rss = GagToAtom().apply(gagJson)


        val xml = XmlSerializer()
        val rssXML: String = xml.toXml(feed).strip()

        Assertions.assertEquals(expected.replace(" ", ""), rssXML.replace(" ", ""))
    }

    @Test
    fun `should mp4 take preference over other`() {
        val gagJson = GagJson(
                GagData(
                        posts = arrayOf(
                                GagPost("id1", "url1", "title1", "Animated", creationTs = 1604378061, images = GagJsonImages(image460sv = GagJsonAnimatedImage("vp9url", h265Url = "h265Url"), image700 = GagJsonPhotoImage("url1"))),
                                GagPost("id2", "url2", "title2", "Photo", creationTs = 1604376133, images = GagJsonImages(image460sv = null, image700 = GagJsonPhotoImage("url2")))),
                        group = GagGroup("comic", "description comic")
                ))
        val feed: Rss = GagToAtom().apply(gagJson)

        Assertions.assertTrue(feed.channel.item[0].description.contains("h265Url"))
    }

}
