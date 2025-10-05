package model.table

import model.enums.BookStatus
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object BookTrackings : IntIdTable("book_tracking", "book_tracking_id") {
    val bookId = integer("book_id")
    val status = enumerationByName("status", 20, BookStatus::class)
    val borrowedAt = timestamp("borrowed_at").nullable()
    val dueDate = timestamp("due_date").nullable()
    val borrowedBy = integer("borrowed_by").nullable()
    val reservedBy = integer("reserved_by").nullable()
    val reservedUntil = timestamp("reserved_until").nullable()
    val isDeleted = bool("is_deleted").default(false)
    val updatedAt = timestamp("updated_at")
}