package ru.clevertec.config

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.clevertec.routes.bookTrackingRoutes

fun Application.configureRouting() {
    routing {
        bookTrackingRoutes()
    }
}
