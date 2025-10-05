package dto.tracking

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.enums.BookStatus
import java.time.Instant

@Serializable
data class BookTrackingDto(
    val id: Int,
    val bookId: Int,
    val status: BookStatus,
    @Contextual val borrowedAt: Instant?,
    @Contextual val dueDate: Instant?,
    val borrowedBy: Int?,
    val reservedBy: Int?,
    @Contextual val reservedUntil: Instant?,
    val isDeleted: Boolean,
    @Contextual val updatedAt: Instant
)