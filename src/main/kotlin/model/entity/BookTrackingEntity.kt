package model.entity

import model.enums.BookStatus
import model.table.BookTrackings
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class BookTrackingEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BookTrackingEntity>(BookTrackings)

    var bookId by BookTrackings.bookId
    var status: BookStatus by BookTrackings.status
    var borrowedAt by BookTrackings.borrowedAt
    var dueDate by BookTrackings.dueDate
    var borrowedBy by BookTrackings.borrowedBy
    var reservedBy by BookTrackings.reservedBy
    var reservedUntil by BookTrackings.reservedUntil
    var isDeleted by BookTrackings.isDeleted
    var updatedAt by BookTrackings.updatedAt
}