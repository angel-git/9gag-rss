package com.ags.client;

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test;

class GagClientTest {

    @Test
    fun `should get json from 9gag`() {
        val client = GagClient()
        val jsonFuture = client.get9GagJson("default")
        val json = jsonFuture?.get()!!
        Assertions.assertNotNull(json.data);
    }
}

