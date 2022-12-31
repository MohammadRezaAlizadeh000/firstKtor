package com.mra

import com.mra.models.Customer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.customerRouting() {
    route(RouteConstance.CUSTOMER_ROUTE) {
        get {
            if (customerStorage.isNotEmpty()) call.respond(customerStorage)
            else call.respondText("No customer yet", status = HttpStatusCode.OK)
        }

        get("{id?}") {
            val id = call.parameters["id"] ?: call.respondText("Wrong Id", status = HttpStatusCode.BadRequest)
            val customer = customerStorage.find { it.id == id } ?: call
                .respondText("No customer with Id $id", status = HttpStatusCode.NotFound)
            call.respond(customer)
        }

        post {
            val customer = call.receive<Customer>()
            if(null != customerStorage.find { it.id == customer.id })
                call.respondText("This user has exist", status = HttpStatusCode.Conflict)
            
            customerStorage.add(customer)
            call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (customerStorage.removeIf { it.id == id }) {
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}