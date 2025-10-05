package dto.tracking

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ReserveBookRequest(
    val userId: Int,
    @Contextual val reservedUntil: Instant
)