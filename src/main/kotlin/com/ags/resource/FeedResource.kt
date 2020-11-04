package com.ags.resource

import com.ags.domain.SupportedGroups
import com.ags.repository.FeedRepository
import com.ags.scheduler.ScheduledOperations
import com.ags.transformer.GagToAtom
import com.ags.transformer.XmlSerializer
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/")
class FeedResource(val scheduler: ScheduledOperations, val repository: FeedRepository, val scheduledOperations: ScheduledOperations) {

    private val rssToString = XmlSerializer()
    private val gagToAtom = GagToAtom()

    @GET
    @Path("feed/{group:.*}")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    fun subscribe(@PathParam("group") group: String ): String {
        val supportedGroups = SupportedGroups.values().map { it.group }
        if (!supportedGroups.contains(group)) {
            throw IllegalArgumentException("You only only subscribe to $supportedGroups but you sent [$group]")
        }
        // TODO use jackson serializer and return RSS instead of string
        return rssToString.toXml(
                gagToAtom.apply(repository.read(group))
        )
    }

    // endpoints to be called from cloud scheduler
    @GET
    @Path("refresh")
    fun refresh() {
        scheduledOperations.updateFeedsFrom9Gag()
    }

    @GET
    @Path("delete")
    fun delete() {
        scheduledOperations.deleteOldData()
    }



}
