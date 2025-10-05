package dto.tracking

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.enums.BookStatus
import java.time.Instant

@Serializable
data class BookTrackingHistoryDto(
    val id: Int,
    val trackingId: Int,
    val bookId: Int,
    val status: BookStatus,
    @Contextual val changedAt: Instant,
    val comment: String?
)