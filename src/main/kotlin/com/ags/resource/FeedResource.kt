package com.ags.resource

import com.ags.domain.ninegag.SupportedGroups
import com.ags.repository.DongleFeedRepository
import com.ags.repository.GagFeedRepository
import com.ags.scheduler.ScheduledOperations
import com.ags.transformer.XmlSerializer
import com.ags.transformer.dongle.DongleToAtom
import com.ags.transformer.ninegag.GagToAtom
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/")
class FeedResource(
    val scheduler: ScheduledOperations,
    val gagRepository: GagFeedRepository,
    val dongleRepository: DongleFeedRepository,
    val scheduledOperations: ScheduledOperations
) {

    private val rssToString = XmlSerializer()
    private val gagToAtom = GagToAtom()
    private val dongleToAtom = DongleToAtom()

    @GET
    @Path("feed/{group:.*}")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    fun subscribe(@PathParam("group") group: String): String {

        // fixme, ugly as hell
        return if (group == "dongle") {
            rssToString.toXml(
                dongleToAtom.apply(dongleRepository.read(group))
            )
        } else {
            val supportedGroups = SupportedGroups.values().map { it.group }
            if (!supportedGroups.contains(group)) {
                throw IllegalArgumentException("You only only subscribe to $supportedGroups but you sent [$group]")
            }
            // TODO use jackson serializer and return RSS instead of string
            rssToString.toXml(
                gagToAtom.apply(gagRepository.read(group))
            )
        }
    }

    // endpoints to be called from cloud scheduler
    @GET
    @Path("refresh")
    fun refresh() {
        scheduledOperations.updateFeeds()
    }

    @GET
    @Path("delete")
    fun delete() {
        scheduledOperations.deleteOldData()
    }


}
