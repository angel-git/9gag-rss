package org.acme.resteasy

import com.ags.client.GagClient
import com.ags.transformer.GagToAtom
import com.ags.transformer.XmlSerializer
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/resteasy")
class ExampleResource(val client: GagClient) {

    private val gagToAtom = GagToAtom()
    private val rssToString = XmlSerializer()



    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String {
        return "hello"
    }


    @GET
    @Path("test")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    fun test(): String {
        val gagJson = client.get9GagJson()?.get()!!
        val rss = gagToAtom.apply(gagJson)
        return rssToString.toXml(rss)
    }

    @GET
    @Path("push")
    @Produces(MediaType.TEXT_PLAIN)
    fun pushRandom(): String {

        return "pushed"
    }



}
