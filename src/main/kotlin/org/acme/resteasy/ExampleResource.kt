package org.acme.resteasy

import com.ags.client.GagClient
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/resteasy")
class ExampleResource(val client: GagClient) {

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
        client.get9GagJson()?.get()
        return ""
    }
}
