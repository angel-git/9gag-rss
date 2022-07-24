package com.ags

import com.ags.domain.donglespace.DonglespaceAllCategoriesJson
import com.ags.domain.donglespace.DonglespaceBettingJson
import com.ags.domain.ninegag.GagJson
import com.google.gson.Gson
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

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
        val gagJson = Gson().fromJson(jsonInput, GagJson::class.java)
        Assertions.assertNotNull(gagJson.data)
    }

    @Test
    fun `it should handle unknown elements`() {
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
        "type": "Unknown",
        "nsfw": 1,
        "upVoteCount": 0,
        "downVoteCount": 0,
        "creationTs": 1603617674,
        "promoted": 0,
        "isVoteMasked": 1,
        "hasLongPostCover": 0,
        "sourceDomain": "",
        "sourceUrl": "",
        "commentsCount": 0,
        "tags": []
      }
    ]
 }}
        """.trimIndent()
        val gagJson = Gson().fromJson(jsonInput, GagJson::class.java)
        Assertions.assertNotNull(gagJson.data)
    }


    @Test
    fun `should parse all categories from dongle`() {

        val inputAllCategoriesJson = """
{
  "data": {
    "allCategories": [
      {
        "code": "funny-aa24ab99-be28-4f6a-920c-cb9dc53cde66",
        "__typename": "Category",
        "name": "FUNNY",
        "parentCode": null,
        "description": "All the funny memes and stories around the globe"
      },
      {
        "code": "news-d5934fd8-05d2-4530-8461-2455ea494203",
        "__typename": "Category",
        "name": "NEWS",
        "parentCode": null,
        "description": "Most interesting news around the world"
      },
      {
        "code": "lifeprotips-31ce53a4-6a89-45ae-ab85-2473b0fd4db4",
        "__typename": "Category",
        "name": "LifeProTips",
        "parentCode": null,
        "description": "Most useful tips that make your life easier"
      },
      {
        "code": "todayilearned-8cd305ef-8530-408a-8d36-4f925039c0c7",
        "__typename": "Category",
        "name": "todayilearned",
        "parentCode": null,
        "description": "Interesting facts. Share what you learned and enrich your knowledge."
      },
      {
        "code": "soccer-08e95ed9-d792-49fc-8337-a3a122259e81",
        "__typename": "Category",
        "name": "SOCCER",
        "parentCode": null,
        "description": "Watch amazing soccer videos and discuss them with friends"
      },
      {
        "code": "sports-49c30ac6-70a2-4723-b45a-de24fffe0b0f",
        "__typename": "Category",
        "name": "SPORTS",
        "parentCode": null,
        "description": "All things sports from videos to news."
      },
      {
        "code": "aww-f919cb6f-b7cf-4f57-982e-5a207cee77ff",
        "__typename": "Category",
        "name": "AWW",
        "parentCode": null,
        "description": "Adorable things from cats to babies"
      },
      {
        "code": "awesome-1258c90c-8d4b-487d-bdac-e17269ab3a4f",
        "__typename": "Category",
        "name": "AWESOME",
        "parentCode": null,
        "description": "Things that make to say awesome"
      },
      {
        "code": "yummy-e843d11a-f649-477f-8770-deabcfbd058c",
        "__typename": "Category",
        "name": "YUMMY",
        "parentCode": null,
        "description": "Share your cooking or just your favorite dishes"
      },
      {
        "code": "original-0e1aa52b-7fe0-4167-9449-4909f211c849",
        "__typename": "Category",
        "name": "ORIGINAL",
        "parentCode": null,
        "description": "Only original content from Dongle Spaces. Be creative."
      },
      {
        "code": "nsfw-f4993b7c-a7b8-416f-b8d3-1e69651ee62a",
        "__typename": "Category",
        "name": "NSFW",
        "parentCode": null,
        "description": "You know what it means.. come inside, alone."
      }
    ]
  }
}""".trimIndent()
        val allCategoriesJson = Gson().fromJson(inputAllCategoriesJson, DonglespaceAllCategoriesJson::class.java)
        Assertions.assertNotNull(allCategoriesJson.data)
        Assertions.assertEquals(allCategoriesJson.data.allCategories.size, 11)
        Assertions.assertEquals(allCategoriesJson.data.allCategories[0].code, "funny-aa24ab99-be28-4f6a-920c-cb9dc53cde66")
        Assertions.assertEquals(allCategoriesJson.data.allCategories[0].name, "FUNNY")
    }

    @Test
    fun `should parse betting from dongle`() {

        val inputBettingJson = """
            {
              "data": {
                "bettings": {
                  "nodes": [
                    {
                      "code": "BET-1658644515128-4fa5eaf38938427185ebe534d2c78d87-N",
                      "title": "Dirty and funny",
                      "__typename": "Betting",
                      "createdAt": "2022-07-24T06:35:15.135Z",
                      "post": {
                        "detail": "<p></p>",
                        "__typename": "BettingPost"
                      },
                      "masterAccountUUID": "6a2fa5e7-2845-41da-9023-3b563904c91b",
                      "masterAccount": {
                        "uuid": "6a2fa5e7-2845-41da-9023-3b563904c91b",
                        "__typename": "UserAccount",
                        "profile": {
                          "accountUUID": "6a2fa5e7-2845-41da-9023-3b563904c91b"
                        }
                      },
                      "categoryCode": "nsfw-f4993b7c-a7b8-416f-b8d3-1e69651ee62a",
                      "category": null,
                      "isAnonymous": false,
                      "upvoteCnt": 54,
                      "downvoteCnt": 0,
                      "myVoteDirection": null,
                      "isMyBookmark": false,
                      "commentCnt": 0
                    },
                    {
                      "code": "BET-1658642932153-3e4683e0d09146d8be02cd89f7da464d-N",
                      "title": "Nice quirk",
                      "__typename": "Betting",
                      "createdAt": "2022-07-24T06:08:52.160Z",
                      "post": {
                        "detail": "<p></p>",
                        "__typename": "BettingPost"
                      },
                      "masterAccountUUID": "64c8e5ad-29f1-424b-818a-de4cc644f005",
                      "masterAccount": {
                        "uuid": "64c8e5ad-29f1-424b-818a-de4cc644f005",
                        "__typename": "UserAccount",
                        "profile": {
                          "accountUUID": "64c8e5ad-29f1-424b-818a-de4cc644f005"
                        }
                      },
                      "categoryCode": "nsfw-f4993b7c-a7b8-416f-b8d3-1e69651ee62a",
                      "category": null,
                      "isAnonymous": false,
                      "upvoteCnt": 83,
                      "downvoteCnt": 1,
                      "myVoteDirection": null,
                      "isMyBookmark": false,
                      "commentCnt": 0
                    }
                  ],
                  "totalCount": 4179,
                  "__typename": "PaginatedBettingModel"
                }
              }
            }
""".trimIndent()
        val bettingJson = Gson().fromJson(inputBettingJson, DonglespaceBettingJson::class.java)
        Assertions.assertNotNull(bettingJson.data)
        Assertions.assertNotNull(bettingJson.data.bettings)
        Assertions.assertNotNull(bettingJson.data.bettings.nodes)
        Assertions.assertEquals(bettingJson.data.bettings.nodes.size, 2)
        Assertions.assertEquals(bettingJson.data.bettings.nodes[0].code, "BET-1658644515128-4fa5eaf38938427185ebe534d2c78d87-N")
        Assertions.assertEquals(bettingJson.data.bettings.nodes[0].title, "Dirty and funny")
        Assertions.assertEquals(bettingJson.data.bettings.nodes[0].post.detail, "<p></p>")
        Assertions.assertEquals(bettingJson.data.bettings.nodes[0].createdAt.toString(), "Sun Jul 24 08:35:15 CEST 2022")
    }

}
