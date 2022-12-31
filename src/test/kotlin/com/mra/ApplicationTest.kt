package com.mra

import com.mra.plugins.configureRouting
import com.mra.plugins.configureSerialization
import io.ktor.server.testing.*
import kotlin.test.Test

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }
//        client.get("/").apply {
//            assertEquals(HttpStatusCode.OK, status)
//            assertEquals("Hello World!", bodyAsText())
//        }
    }
}