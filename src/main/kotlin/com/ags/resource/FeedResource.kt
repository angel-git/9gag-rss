package com.ags.resource

import com.ags.client.GagClient
import com.ags.transformer.GagToAtom
import com.ags.transformer.XmlSerializer
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/feed")
class FeedResource(val client: GagClient) {

    private val gagToAtom = GagToAtom()
    private val rssToString = XmlSerializer()


    @GET
    @Path("/nsfw")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    fun nsfw(): String {
        val gagJson = client.get9GagJson("nsfw")?.get()!!
        val rss = gagToAtom.apply(gagJson)
        return rssToString.toXml(rss)
    }

    @GET
    @Path("/comic")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    fun comic(): String {
        val gagJson = client.get9GagJson("comic")?.get()!!
        val rss = gagToAtom.apply(gagJson)
        return rssToString.toXml(rss)
    }


}
