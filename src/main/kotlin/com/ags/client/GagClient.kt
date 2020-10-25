package com.ags.client

import com.ags.domain.GagJson
import com.ags.domain.GagPost
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
                        .header("Cookie","_fbp=fb.1.1600765635993.254185525; _ga=GA1.2.1500693989.1600765636; _gid=GA1.2.1545355289.1603455418; _pk_id.7.f7ab=e0c9e8bdba1ddc4f.1600765636.16.1603535146.1603535146.; gag_tz=2; sign_up_referer=; __gads=ID=dde8ecc7fb771d3a-22de4c680bb90044:T=1603455749:RT=1603455749:S=ALNI_Ma5V2-j3CuNzquq50adSHDVSUPTRA; __qca=P0-315258375-1603455748223; addtl_consent=1~39.4.3.9.6.5.4.13.6.4.15.9.5.2.7.4.1.7.1.3.2.10.3.5.4.13.8.4.6.9.7.10.2.9.2.12.6.7.6.14.5.20.6.5.1.3.1.11.29.4.14.4.4.1.3.10.6.2.9.6.6.4.5.3.1.4.29.4.5.3.1.6.2.2.17.1.17.10.9.1.8.3.3.2.8.1.2.1.3.142.4.8.35.7.15.1.14.3.1.8.10.14.11.3.7.25.5.18.9.7.41.2.4.18.21.3.4.2.1.6.6.5.2.14.18.7.3.2.2.8.19.1.8.8.6.3.10.4.5.15.2.4.9.3.1.6.4.11.1.3.18.4.16.2.6.8.2.4.11.6.5.5.12.16.11.8.1.10.28.8.4.1.3.21.2.7.6.1.9.30.17.4.9.15.8.7.3.6.6.7.2.4.1.7.12.10.3.22.13.2.12.2.4.6.1.4.15.2.4.9.4.5.1.3.7.13.5.3.12.4.13.4.14.8.2.15.2.5.5.1.2.2.1.1.1.14.7.4.8.2.9.9.1.18.12.13.2.18.1.1.3.1.1.9.20.5.4.6.14.8.4.5.3.5.4.8.4.2.2.2.14.2.13.4.2.6.9.6.3.4.3.5.2.3.6.10.11.2.4.3.16.3.8.3.3.1.2.3.9.19.11.15.3.10.7.6.4.3.4.9.3.3.3.1.1.1.6.11.3.1.1.7.4.3.3.1.10.5.2.6.3.2.1.1.4.3.2.2.7.2.13.7.12.2.1.6.4.5.4.3.2.2.4.1.3.1.1.1.5.6.1.6.9.1.4.1.2.1.7.2.8.3.8.1.3.1.1.2.1.3.2.6.1.5.6.1.5.3.1.3.1.1.2.2.7.7.1.4.1.2.6.1.2.1.1.3.1.1.4.1.1.2.1.8.1.3.4.4.1.2.2.1.3.1.4.3.9.6.1.15.10.28.1.2.1.1.12.3.4.1.5.1.3.4.7.1.3.1.1.3.1.5.3.1.3.2.2.1.1.4.2.1.2.1.1.1.2.2.4.2.1.2.2.2.4.1.1.1.2.1.1.1.1.1.1.1.1.1.1.1.2.2.1.1.2.1.2.1.7.1.2.1.1.1.2.1.1.1.1.2.1.1.3.2.1.1.2.6.1.1.1.5.2.1.6.5.1.1.1.1.1.2.1.1.3.1.1.4.1.1.2.2.1.1.4.2.1.1.2.3.2.1.2.3.1.1.1.1.4.1.1.1.5.1.8.1.3.1.5.1.1.3.2.1.1.1.2.3.1.4.2.1.2.2.2.1.1.1.1.1.1.11.1.3.1.1.2.2.1.4.2.2.1.2.1.4.1.1.1.1.1.3.2.1.1.2.5.1.3.6.4.1.1.3.1.4.3.1.2.2.5.1.7.4.1.1.1.1.1.1.4.2.1.14; euconsent-v2=CO7u-FLO7u-FLAKASAENA9CsAP_AAH_AAA5QG9td_X_fb39j-_59_9t0eY1f9_7_v20zjgeds-8Nyd_X_L8X4mM7vB36pq4KuR4Eu3LBAQFlHOHcTQmw6IkVqTPsak2Mr7NKJ7PEilMbO2dYGHtfn9VTuZKY797s___z__-_____75f_r-3_3_vp9V-BugBJhqXwEWYljASTRpVCiBCFcSHQAgAooRhaJrCAlcFOyuAj9BAwAQGoCMCIEGIKMWQQAAAABJREAIAeCARAEQCAAEAKkBCAAjQBBYASBgEAAoBoWAEUAQgSEGRwVHKYEBEi0UE8kYAlFzsYYQhlFAAA.f_gACfgAAAAA; __cfduid=d38522bd86dc3995c3fd1765dce1f0ff21603455417; cto_bidid=JfUsgV9pSHZCY1lENFFFSWNkM1Y0R2tFTEdEVTVKS3hhQzZtM28wMWhvc0JHVCUyRmI3d2ZlVGVBSEQlMkZmQ2Vrc21CQXQ4RWN4MjdreGtuSSUyRld1ajVLdmtIbXU1QSUzRCUzRA; cto_bundle=EeHp_V8lMkZQRWJLcGxQYkJZampRVFZBT0x2am1UcDNFaHV0dU9BV0FmRU5zMlJXYzk2aUY2OGI0S1dITnh3bXhRemxwVWhSaFpGTXJmaDJkUVBMSkhaJTJGdGttMEdsNzRYJTJGYTRlbjdUME1mQXdKNyUyQkIyOHJjU1VFVUhhRlhMVXlzbDJDUXNP; cto_test_cookie=; ____lo=NL; ____ri=7162")
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
