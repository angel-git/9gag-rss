package com.ags.client

import com.ags.domain.GagPost
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PubSubClient(@ConfigProperty(name = "deployed.url") private val deployedUrl: String) {

    private val executor: ExecutorService = Executors.newFixedThreadPool(1)

    private val httpClient: HttpClient = HttpClient
            .newBuilder()
            .executor(executor)
            .version(HttpClient.Version.HTTP_2)
            .build()

    fun notifyAppspot(group: String, posts: List<GagPost>) {
        if (posts.isNotEmpty()) {
            httpClient.sendAsync(
                    HttpRequest.newBuilder()
                            .POST(HttpRequest.BodyPublishers.ofString(makePostBody(group))).uri(URI.create("https://pubsubhubbub.appspot.com/"))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            )
        }
    }

    private fun makePostBody(group: String): String {
        return "hub.mode=publish&hub.url=$deployedUrl/feed/$group"
    }
}
