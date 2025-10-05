package dto.tracking

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import model.enums.BookStatus
import java.time.LocalDate

@Serializable
data class UpdateBookStatusRequest(
    val status: BookStatus,
    val borrowedBy: Int? = null,
    @Contextual val dueDate: LocalDate? = null
)