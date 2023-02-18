package com.ags.client

import com.ags.domain.GagGroup
import com.ags.domain.GagJson
import com.ags.domain.SupportedGroups
import com.google.gson.Gson
import org.brotli.dec.BrotliInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class GagClient {

    private val executor: ExecutorService = Executors.newFixedThreadPool(5)

    private val httpClient: HttpClient = HttpClient
            .newBuilder()
            .executor(executor)
            .version(HttpClient.Version.HTTP_2)
            .build()

    private fun url(group: String) = "https://9gag.com/v1/group-posts/group/$group/type/hot"

    fun get9GagJson(group: String): CompletableFuture<GagJson>? {
        return httpClient.sendAsync(
                HttpRequest.newBuilder().GET().uri(URI.create(url(group)))
                        .header("Cookie", " ____ri=295; ____lo=NL")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                        .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.50")
                        .header("Accept-Language", "en-GB,en;q=0.9")
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

    // https://github.com/google/brotli/blob/master/java/org/brotli/dec/BitReaderTest.java
    @Throws(IOException::class)
    private fun decompress(data: ByteArray, byByte: Boolean): ByteArray {
        val buffer = ByteArray(65536)
        val input = ByteArrayInputStream(data)
        val output = ByteArrayOutputStream()
        val brotliInput = BrotliInputStream(input)
        if (byByte) {
            val oneByte = ByteArray(1)
            while (true) {
                val next = brotliInput.read()
                if (next == -1) {
                    break
                }
                oneByte[0] = next.toByte()
                output.write(oneByte, 0, 1)
            }
        } else {
            while (true) {
                val len = brotliInput.read(buffer, 0, buffer.size)
                if (len <= 0) {
                    break
                }
                output.write(buffer, 0, len)
            }
        }
        brotliInput.close()
        return output.toByteArray()
    }

}
