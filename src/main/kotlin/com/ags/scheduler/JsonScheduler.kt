package com.ags.scheduler

import com.ags.client.GagClient
import com.ags.domain.GagJson
import com.ags.repository.FeedRepository
import io.quarkus.scheduler.Scheduled
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class JsonScheduler(val client: GagClient, val repository: FeedRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    // TODO move this away from here
    private val groupsSupported = listOf("nsfw", "comic")

    @Scheduled(every = "300s")
    fun getMeSomeJson() {
        logger.info("Refreshing RSS from 9gag")
        groupsSupported.forEach {
            repository.add(it, fetchRss(it))
        }
    }

    private fun fetchRss(group: String): GagJson {
        val json = client.get9GagJson(group)?.get()!!
        logger.info("Refreshed RSS from 9gag $group, found ${json.data.posts.size} posts")
        return json
    }

}
