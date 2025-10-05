package dto.tracking

import kotlinx.serialization.Serializable
import model.enums.BookStatus

@Serializable
data class CreateBookTrackingRequest(
    val bookId: Int,
    val status: BookStatus = BookStatus.AVAILABLE
)