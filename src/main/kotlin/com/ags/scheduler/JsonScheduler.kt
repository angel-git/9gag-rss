package com.ags.scheduler

import com.ags.client.GagClient
import com.ags.domain.GagJson
import com.ags.domain.SupportedGroups
import com.ags.repository.FeedRepository
import io.quarkus.scheduler.Scheduled
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class JsonScheduler(val client: GagClient, val repository: FeedRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(every = "300s")
    fun getMeSomeJson() {
        logger.info("Refreshing RSS from 9gag")
        SupportedGroups.values().forEach {
            repository.add(it.group, fetchRss(it.group))
        }
    }

    @Scheduled(every = "12h")
    fun deleteOldData() {
        logger.info("going to delete old posts")
        repository.deleteDayOldPosts()
    }

    private fun fetchRss(group: String): GagJson {
        val json = client.get9GagJson(group)?.get()!!
        logger.info("Refreshed RSS from 9gag $group, found ${json.data.posts.size} posts")
        return json
    }

}
