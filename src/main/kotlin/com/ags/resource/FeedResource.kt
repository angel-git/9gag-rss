package com.ags.resource

import com.ags.scheduler.JsonScheduler
import com.ags.transformer.XmlSerializer
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/feed")
class FeedResource(val scheduler: JsonScheduler) {

    private val rssToString = XmlSerializer()

    @GET
    @Path("/nsfw")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    fun nsfw(): String {
       return rssToString.toXml(scheduler.getMeRss("nsfw"))
    }

    @GET
    @Path("/comic")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    fun comic(): String {
        return rssToString.toXml(scheduler.getMeRss("comic"))
    }


}
