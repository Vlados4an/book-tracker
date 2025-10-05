package dto.tracking

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.enums.BookStatus
import java.time.Instant

@Serializable
data class UpdateBookStatusRequest(
    val status: BookStatus,
    val borrowedBy: Int? = null,
    @Contextual val dueDate: Instant? = null
)