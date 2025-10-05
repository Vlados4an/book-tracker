package model.entity

import model.enums.BookStatus
import model.table.BookTrackingHistories
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class BookTrackingHistoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BookTrackingHistoryEntity>(BookTrackingHistories)

    var tracking by BookTrackingEntity referencedOn BookTrackingHistories.tracking
    var status: BookStatus by BookTrackingHistories.status
    var changedAt by BookTrackingHistories.changedAt
    var comment by BookTrackingHistories.comment
}