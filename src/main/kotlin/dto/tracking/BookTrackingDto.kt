package dto.tracking

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.enums.BookStatus
import java.time.LocalDate

@Serializable
data class BookTrackingDto(
    val id: Int,
    val bookId: Int,
    val status: BookStatus,
    @Contextual val borrowedAt: LocalDate?,
    @Contextual val dueDate: LocalDate?,
    val borrowedBy: Int?,
    val reservedBy: Int?,
    @Contextual val reservedUntil: LocalDate?,
    val isDeleted: Boolean,
    @Contextual val updatedAt: LocalDate
)