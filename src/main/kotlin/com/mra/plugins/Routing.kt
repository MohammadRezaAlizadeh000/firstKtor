package com.mra.plugins

import com.mra.customerRouting
import com.mra.getOrderRute
import com.mra.listOrderRoute
import com.mra.totalizeOrderRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
//        get("/") {
//            call.respondText("Hello World!")
//        }

        customerRouting()
        getOrderRute()
        listOrderRoute()
        totalizeOrderRoute()

    }
}
