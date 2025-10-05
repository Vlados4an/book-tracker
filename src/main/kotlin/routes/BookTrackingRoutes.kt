package ru.clevertec.routes

import dto.tracking.CreateBookTrackingRequest
import dto.tracking.ReserveBookRequest
import dto.tracking.UpdateBookStatusRequest
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.clevertec.service.BookTrackingService
import ru.clevertec.validator.validatedReceive
import util.getIntParamOrBadRequest

fun Route.bookTrackingRoutes() {
    val trackingService by closestDI().instance<BookTrackingService>()

    route("/api/v1/tracking") {

        post {
            val req = call.validatedReceive<CreateBookTrackingRequest>()
            val dto = trackingService.create(req)
            call.respond(HttpStatusCode.Created, dto)
        }

        get("/available") {
            call.respond(trackingService.getAvailable())
        }

        get("/{bookId}") {
            val id = call.getIntParamOrBadRequest("bookId")
            val dto = trackingService.get(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(dto)
        }

        put("/{bookId}/status") {
            val id = call.getIntParamOrBadRequest("bookId")
            val req = call.validatedReceive<UpdateBookStatusRequest>()
            val dto = trackingService.updateStatus(id, req) ?: return@put call.respond(HttpStatusCode.NotFound)
            call.respond(dto)
        }

        post("/{bookId}/reserve") {
            val id = call.getIntParamOrBadRequest("bookId")
            val req = call.validatedReceive<ReserveBookRequest>()
            val dto = trackingService.reserve(id, req)
                ?: return@post call.respond(HttpStatusCode.BadRequest)
            call.respond(dto)
        }

        delete("/{bookId}") {
            val id = call.getIntParamOrBadRequest("bookId")
            val ok = trackingService.delete(id)
            if (!ok) return@delete call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.NoContent)
        }

        get("/history/{bookId}") {
            val id = call.getIntParamOrBadRequest("bookId")
            call.respond(trackingService.history(id))
        }
    }
}
