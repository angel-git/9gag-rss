package com.ags.scheduler

import com.ags.client.GagClient
import com.ags.client.PubSubClient
import com.ags.domain.GagJson
import com.ags.domain.ninegag.SupportedGroups
import com.ags.repository.FeedRepository
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ScheduledOperations(
        val client: GagClient,
        val repository: FeedRepository,
        val pubSubClient: PubSubClient
        ) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun updateFeedsFrom9Gag() {
        logger.info("Refreshing RSS from 9gag")
        SupportedGroups.values().forEach {
            val updatedPosts = repository.add(it.group, fetchRss(it.group))
            logger.info("Notifying for [${it.group}] with [${updatedPosts.size}] created posts")
            pubSubClient.notifyAppspot(it.group, updatedPosts)
        }
    }

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
