package dto.kafka

import config.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class NotificationEvent(
    val bookId: Int,
    val reservedBy: Int,
    @Serializable(with = LocalDateSerializer::class)
    val reservedUntil: LocalDate,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)