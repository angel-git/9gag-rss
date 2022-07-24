package com.ags.scheduler

import com.ags.client.DongleClient
import com.ags.client.GagClient
import com.ags.client.PubSubClient
import com.ags.domain.donglespace.DonglespaceBettingJson
import com.ags.domain.ninegag.GagJson
import com.ags.domain.ninegag.SupportedGroups
import com.ags.repository.DongleFeedRepository
import com.ags.repository.GagFeedRepository
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ScheduledOperations(
    val gagClient: GagClient,
    val dongleClient: DongleClient,
    val gagRepository: GagFeedRepository,
    val dongleRepository: DongleFeedRepository,
    val pubSubClient: PubSubClient
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun deleteOldData() {
        logger.info("going to delete old posts")
        gagRepository.deleteDayOldPosts()
        dongleRepository.deleteDayOldPosts()
    }

    fun updateFeeds() {
        updateFeedsFrom9Gag()
        updateFeedsFromDongle()
    }

    private fun updateFeedsFrom9Gag() {
        logger.info("Refreshing RSS from 9gag")
        SupportedGroups.values().forEach {
            val updatedPosts = gagRepository.add(it.group, fetch9GagRss(it.group))
            logger.info("Notifying for [${it.group}] with [${updatedPosts.size}] created posts")
            pubSubClient.notifyAppspot(it.group, updatedPosts)
        }
    }

    private fun fetch9GagRss(group: String): GagJson {
        val json = gagClient.getJson(group)?.get()!!
        logger.info("Refreshed RSS from 9gag $group, found ${json.data.posts.size} posts")
        return json
    }

    private fun updateFeedsFromDongle() {
        logger.info("Refreshing RSS from dongle")
        val updatedPosts = dongleRepository.add("dongle", fetchDongle())
        logger.info("Notifying for [dongle] with [${updatedPosts.size}] created posts")
        pubSubClient.notifyAppspot("dongle", updatedPosts)
    }

    private fun fetchDongle(): DonglespaceBettingJson {
        val json = dongleClient.getJson("NSFW")?.get()!!
        logger.info("Refreshed RSS from dongle, found ${json.data.bettings.nodes.size} posts")
        return json
    }

}
