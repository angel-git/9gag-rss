package com.ags.resource

import com.ags.repository.FeedRepository
import com.ags.scheduler.JsonScheduler
import com.ags.transformer.GagToAtom
import com.ags.transformer.XmlSerializer
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/feed")
class FeedResource(val scheduler: JsonScheduler, val repository: FeedRepository) {

    private val rssToString = XmlSerializer()
    private val gagToAtom = GagToAtom()

    @GET
    @Path("/nsfw")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    fun nsfw(): String {
        return rssToString.toXml(
                gagToAtom.apply(repository.read("nsfw"))
        )
    }

    @GET
    @Path("/comic")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    fun comic(): String {
        return rssToString.toXml(
                gagToAtom.apply(repository.read("comic"))
        )
    }


}
