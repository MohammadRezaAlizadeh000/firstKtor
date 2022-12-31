package com.mra

import com.mra.RouteConstance.SINGLE_ORDER_ROUTE
import com.mra.RouteConstance.TOTAL_ORDER_ROUTE
import com.mra.models.Customer
import com.mra.models.orderStorage
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
            if (null != customerStorage.find { it.id == customer.id })
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


fun Route.listOrderRoute() {
    route(RouteConstance.ORDERS_ROUTE) {
        get {
            if (orderStorage.isNotEmpty()) call.respond(orderStorage)
            else call.respondText("No order yet", status = HttpStatusCode.OK)
        }
    }
}

fun Route.getOrderRute() {
    get(SINGLE_ORDER_ROUTE) {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)
        val order = orderStorage.find { it.number == id } ?: return@get call.respondText(
            "Not Found",
            status = HttpStatusCode.NotFound
        )
        call.respond(order)
    }
}

fun Route.totalizeOrderRoute() {
    get(TOTAL_ORDER_ROUTE) {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status = HttpStatusCode.BadRequest)
        val order = orderStorage.find { it.number == id } ?: return@get call.respondText(
            "Not Found",
            status = HttpStatusCode.NotFound
        )
        val total = order.contents.sumOf { it.price * it.amount }
        call.respond(total)
    }
}