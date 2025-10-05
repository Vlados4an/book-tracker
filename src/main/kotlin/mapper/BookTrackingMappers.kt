package mapper

import dto.tracking.BookTrackingDto
import dto.tracking.BookTrackingHistoryDto
import model.entity.BookTrackingEntity
import model.entity.BookTrackingHistoryEntity

fun BookTrackingEntity.toDto() = BookTrackingDto(
    id = id.value,
    bookId = bookId,
    status = status,
    borrowedAt = borrowedAt,
    dueDate = dueDate,
    borrowedBy = borrowedBy,
    reservedBy = reservedBy,
    reservedUntil = reservedUntil,
    isDeleted = isDeleted,
    updatedAt = updatedAt
)

fun BookTrackingHistoryEntity.toDto() = BookTrackingHistoryDto(
    id = id.value,
    trackingId = tracking.id.value,
    bookId = tracking.bookId,
    status = status,
    changedAt = changedAt,
    comment = comment
)