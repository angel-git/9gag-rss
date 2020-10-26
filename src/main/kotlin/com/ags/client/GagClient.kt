package com.ags.client

import com.ags.domain.GagJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
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

    private val url = "https://9gag.com/v1/group-posts/group/nsfw/type/fresh"

    fun get9GagJson(): CompletableFuture<GagJson>? {
        return httpClient.sendAsync(
                HttpRequest.newBuilder().GET().uri(URI.create(url))
                        .header("Cookie","__cfduid=d4c4bfc44826d9a80c77561e5428abc591603692951; ____ri=4775; ____lo=NL")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Safari/605.1.15")
                        .header("Accept-Language", "en-us")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .build(),
                HttpResponse.BodyHandlers.ofByteArray())
                .thenApply { decompress(it.body(), false) }
                .thenApply { String(it) }
                .thenApply { Json { ignoreUnknownKeys=true }.decodeFromString<GagJson>(it) }
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
