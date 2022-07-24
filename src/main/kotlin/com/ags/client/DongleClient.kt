package com.ags.client

import com.ags.domain.donglespace.DonglespaceAllCategoriesJson
import com.ags.domain.donglespace.DonglespaceBettingJson
import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DongleClient {

    private val client: Client = Client()

    private val dongleApiUrl = "https://api.donglespace.com/graphql"

    private fun bettingsBody(categoryCode: String): String {
        return "{\"operationName\":\"Bettings\",\"variables\":{\"limit\":5,\"fieldSet\":{\"categoryCode\":\"${categoryCode}\"},\"excludeDownvotedPosts\":true},\"query\":\"query Bettings(\$offset: Int, \$limit: Int, \$fieldSet: BettingFieldSet, \$excludeDownvotedPosts: Boolean) {\\n  bettings(\\n    offset: \$offset\\n    limit: \$limit\\n    fieldSet: \$fieldSet\\n    excludeDownvotedPosts: \$excludeDownvotedPosts\\n  ) {\\n    nodes {\\n      ...BettingSummary\\n      __typename\\n    }\\n    totalCount\\n    __typename\\n  }\\n}\\n\\nfragment BettingSummary on Betting {\\n  ...BettingBase\\n  createdAt\\n  post {\\n    detail\\n    __typename\\n  }\\n  masterAccountUUID\\n  masterAccount {\\n    ...UserAccountSummary\\n    __typename\\n  }\\n  categoryCode\\n  category {\\n    ...CategorySummary\\n    __typename\\n  }\\n  isAnonymous\\n  upvoteCnt\\n  downvoteCnt\\n  myVoteDirection\\n  isMyBookmark\\n  commentCnt\\n  __typename\\n}\\n\\nfragment BettingBase on Betting {\\n  code\\n  title\\n  __typename\\n}\\n\\nfragment UserAccountSummary on UserAccount {\\n  ...UserAccountBase\\n  profile {\\n    ...UserAccountProfileSummary\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment UserAccountBase on UserAccount {\\n  uuid\\n  __typename\\n}\\n\\nfragment UserAccountProfileSummary on UserAccountProfile {\\n  ...UserAccountProfileBase\\n  data {\\n    name\\n    nickname\\n    email\\n    imageUrl\\n    isHidden\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment UserAccountProfileBase on UserAccountProfile {\\n  accountUUID\\n  code\\n  __typename\\n}\\n\\nfragment CategorySummary on Category {\\n  ...CategoryBase\\n  name\\n  parentCode\\n  __typename\\n}\\n\\nfragment CategoryBase on Category {\\n  code\\n  __typename\\n}\\n\"}"
    }

    fun getJson(group: String): CompletableFuture<DonglespaceBettingJson>? {
        return client.httpClient.sendAsync(
            HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"operationName\":\"AllCategories\",\"variables\":{\"sort\":{\"by\":\"PRIOR\"}},\"query\":\"query AllCategories(\$sort: SortInput) {\\n  allCategories(sort: \$sort) {\\n    ...CategoryDetail\\n    __typename\\n  }\\n}\\n\\nfragment CategoryDetail on Category {\\n  ...CategorySummary\\n  description\\n  __typename\\n}\\n\\nfragment CategorySummary on Category {\\n  ...CategoryBase\\n  name\\n  parentCode\\n  __typename\\n}\\n\\nfragment CategoryBase on Category {\\n  code\\n  __typename\\n}\\n\"}"))
                .uri(URI.create(dongleApiUrl))
                .header("Accept", "*/*")
                .header("content-type", "application/json")
                .header(
                    "User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Safari/605.1.15"
                )
                .header("Accept-Language", "en-us")
                .header("Accept-Encoding", "gzip, deflate, br")
                .build(),
            HttpResponse.BodyHandlers.ofByteArray()
        )
            .thenApply { client.decompress(it.body(), false) }
            .thenApply { String(it) }
            .thenApply {
                Gson().fromJson(
                    it,
                    DonglespaceAllCategoriesJson::class.java
                )

            }
            .thenApply {
                val nsfwCategoryCode = it.data.allCategories.find { it.name == "NSFW" }!!.code
                client.httpClient.sendAsync(
                    HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(bettingsBody(nsfwCategoryCode)))
                        .uri(URI.create(dongleApiUrl))
                        .header("Accept", "*/*")
                        .header("content-type", "application/json")
                        .header(
                            "User-Agent",
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Safari/605.1.15"
                        )
                        .header("Accept-Language", "en-us")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .build(),
                    HttpResponse.BodyHandlers.ofByteArray()
                )
                    .thenApply { client.decompress(it.body(), false) }
                    .thenApply { String(it) }
                    .thenApply {
                        Gson().fromJson(
                            it,
                            DonglespaceBettingJson::class.java
                        )
                    }.get()
            }
            .toCompletableFuture()
    }
}