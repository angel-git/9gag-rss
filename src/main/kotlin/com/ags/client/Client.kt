package com.ags.client

import org.brotli.dec.BrotliInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.http.HttpClient
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

abstract class Client<T> {

    abstract fun getJson(group: String): CompletableFuture<T>?

    private val executor: ExecutorService = Executors.newFixedThreadPool(5)

    protected val httpClient: HttpClient = HttpClient
        .newBuilder()
        .executor(executor)
        .version(HttpClient.Version.HTTP_2)
        .build()

    // https://github.com/google/brotli/blob/master/java/org/brotli/dec/BitReaderTest.java
    @Throws(IOException::class)
    fun decompress(data: ByteArray, byByte: Boolean): ByteArray {
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