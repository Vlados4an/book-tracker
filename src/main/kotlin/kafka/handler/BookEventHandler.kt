package kafka.handler

import dto.tracking.CreateBookTrackingRequest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import model.enums.BookStatus
import org.slf4j.LoggerFactory
import ru.clevertec.dto.kafka.BookEvent
import ru.clevertec.kafka.handler.EventHandler
import ru.clevertec.service.tracking.BookTrackingService

class BookEventHandler(
    private val trackingService: BookTrackingService
) : EventHandler<BookEvent> {
    
    private val logger = LoggerFactory.getLogger(BookEventHandler::class.java)
    private val json = Json { ignoreUnknownKeys = true }
    
    override suspend fun handle(eventJson: String) {
        try {
            val jsonElement = json.parseToJsonElement(eventJson).jsonObject
            val eventType = jsonElement["type"]?.jsonPrimitive?.content
            
            when (eventType) {
                "BOOK_CREATED" -> {
                    val event = json.decodeFromString<BookEvent.Created>(eventJson)
                    logger.info("Processing BOOK_CREATED: bookId=${event.bookId}")
                    trackingService.create(
                        CreateBookTrackingRequest(
                            bookId = event.bookId,
                            status = BookStatus.AVAILABLE
                        )
                    )
                }
                "BOOK_UPDATED" -> {
                    val event = json.decodeFromString<BookEvent.Updated>(eventJson)
                    logger.info("Processing BOOK_UPDATED: bookId=${event.bookId}")
                }
                "BOOK_DELETED" -> {
                    val event = json.decodeFromString<BookEvent.Deleted>(eventJson)
                    logger.info("Processing BOOK_DELETED: bookId=${event.bookId}")
                    trackingService.delete(event.bookId)
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to handle event", e)
            throw e
        }
    }
}