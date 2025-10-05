package dto.tracking

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.enums.BookStatus
import java.time.Instant
import java.time.LocalDate

@Serializable
data class BookTrackingHistoryDto(
    val id: Int,
    val trackingId: Int,
    val bookId: Int,
    val status: BookStatus,
    @Contextual val changedAt: LocalDate,
    val comment: String?
)