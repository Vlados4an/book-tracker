package model.table

import model.enums.BookStatus
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

object BookTrackingHistories : IntIdTable("book_tracking_history", "book_tracking_history_id") {
    val tracking = reference("book_tracking_id", BookTrackings)
    val status = enumerationByName("status", 20, BookStatus::class)
    val changedAt = date("changed_at")
    val comment = varchar("comment", 255).nullable()
}