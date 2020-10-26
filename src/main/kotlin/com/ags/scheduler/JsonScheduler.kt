package com.ags.scheduler

import com.ags.client.GagClient
import com.ags.domain.Rss
import com.ags.transformer.GagToAtom
import io.quarkus.scheduler.Scheduled
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class JsonScheduler(val client: GagClient) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val gagToAtom = GagToAtom()

    // TODO move this away from here
    private val groupsSupported = listOf("nsfw", "comic")

    // TODO this should be save in database
    private val rssFetched = mutableMapOf<String, Rss>()

    @Scheduled(every = "300s")
    fun getMeSomeJson() {
        logger.info("Refreshing RSS from 9gag")
        groupsSupported.forEach {
            rssFetched[it] = fetchRss(it)
        }
    }

    // TODO remove this method once we have database
    fun getMeRss(group: String): Rss {
        return rssFetched.computeIfAbsent(group) { fetchRss(group) }
    }

    private fun fetchRss(group: String): Rss {
        val json = client.get9GagJson(group)?.get()!!
        logger.info("Refreshed RSS from 9gag $group, found ${json.data.posts.size} posts")
        return gagToAtom.apply(json)
    }

}
