package dto.tracking

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.enums.BookStatus
import java.time.LocalDate

@Serializable
data class CreateBookTrackingRequest(
    val bookId: Int,
    val status: BookStatus = BookStatus.AVAILABLE,
    @Contextual val borrowedAt: LocalDate? = null,
    @Contextual val dueDate: LocalDate? = null,
    val borrowedBy: Int? = null,
    val reservedBy: Int? = null,
    @Contextual val reservedUntil: LocalDate? = null
)