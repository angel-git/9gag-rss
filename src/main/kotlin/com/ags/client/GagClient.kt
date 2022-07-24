package com.ags.client

import com.ags.domain.ninegag.GagGroup
import com.ags.domain.ninegag.GagJson
import com.ags.domain.ninegag.SupportedGroups
import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class GagClient: Client<GagJson>() {

    private fun url(group: String) = "https://9gag.com/v1/group-posts/group/$group/type/hot"

    override fun getJson(group: String): CompletableFuture<GagJson>? {
        return httpClient.sendAsync(
                HttpRequest.newBuilder().GET().uri(URI.create(url(group)))
                        .header("Cookie", "__cfduid=d4c4bfc44826d9a80c77561e5428abc591603692951; ____ri=4775; ____lo=NL")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Safari/605.1.15")
                        .header("Accept-Language", "en-us")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .build(),
                HttpResponse.BodyHandlers.ofByteArray())
                .thenApply { decompress(it.body(), false) }
                .thenApply { String(it) }
                .thenApply { Gson().fromJson(it, GagJson::class.java) } // can't use kotlinx.serialization.json.Json in native
                .thenApply {
                    // fresh topics from default don't have group
                    if (group == SupportedGroups.DEFAULT.group) {
                        it.data.group = GagGroup("hot - default", "what's been getting a lot of upvotes / comments / shares / views recently")
                    }
                    it
                }
                .toCompletableFuture()

    }

}
