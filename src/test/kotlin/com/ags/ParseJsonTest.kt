package com.ags

import com.ags.domain.GagJson
import io.quarkus.test.junit.QuarkusTest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@QuarkusTest
class ParseJsonTest {

    @Test
    fun `should parse json into domain classes`() {

        val jsonInput = """
            {
  "meta": {
    "timestamp": 1603617768,
    "status": "Success",
    "sid": "9gVQ01EVjlHTUVkMMRVUzwEVJlnT31TY"
  },
  "data": {
    "posts": [
      {
        "id": "aBmbwO2",
        "url": "http:\/\/9gag.com\/gag\/aBmbwO2",
        "title": "Sunday morning teasing.",
        "description": "",
        "type": "Photo",
        "nsfw": 1,
        "upVoteCount": 0,
        "downVoteCount": 0,
        "creationTs": 1603617674,
        "promoted": 0,
        "isVoteMasked": 1,
        "hasLongPostCover": 0,
        "images": {
          "image700": {
            "width": 700,
            "height": 1112,
            "url": "https:\/\/img-9gag-fun.9cache.com\/photo\/aBmbwO2_700b.jpg",
            "webpUrl": "https:\/\/img-9gag-fun.9cache.com\/photo\/aBmbwO2_700bwp.webp"
          },
          "image460": {
            "width": 460,
            "height": 731,
            "url": "https:\/\/img-9gag-fun.9cache.com\/photo\/aBmbwO2_460s.jpg",
            "webpUrl": "https:\/\/img-9gag-fun.9cache.com\/photo\/aBmbwO2_460swp.webp"
          },
          "imageFbThumbnail": {
            "width": 220,
            "height": 220,
            "url": "https:\/\/img-9gag-fun.9cache.com\/photo\/aBmbwO2_fbthumbnail.jpg"
          }
        },
        "sourceDomain": "",
        "sourceUrl": "",
        "commentsCount": 0,
        "postSection": {
          "name": "NSFW",
          "url": "https:\/\/9gag.com\/nsfw",
          "imageUrl": "https:\/\/miscmedia-9gag-fun.9cache.com\/images\/thumbnail-facebook\/1557297099.4728_VeSAvU_100x100.jpg",
          "webpUrl": "https:\/\/miscmedia-9gag-fun.9cache.com\/images\/thumbnail-facebook\/1557297099.4728_VeSAvU_100x100wp.webp"
        },
        "tags": []
      }
    ],
    "group": {
      "name": "NSFW",
      "url": "nsfw",
      "description": "Not Safe For Work. No sexually explicit content.",
      "ogImageUrl": "https:\/\/miscmedia-9gag-fun.9cache.com\/images\/thumbnail-facebook\/1557297099.4728_VeSAvU_100x100.jpg",
      "ogWebpUrl": "https:\/\/miscmedia-9gag-fun.9cache.com\/images\/thumbnail-facebook\/1557297099.4728_VeSAvU_100x100wp.webp",
      "userUploadEnabled": true,
      "isSensitive": true,
      "location": ""
    }
 }}
        """.trimIndent()
        val gagJson = Json { ignoreUnknownKeys=true }.decodeFromString<GagJson>(jsonInput)
        Assertions.assertNotNull(gagJson.data)
    }

}
