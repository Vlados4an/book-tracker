package ru.clevertec.repository

import dto.tracking.ReserveBookRequest
import dto.tracking.UpdateBookStatusRequest
import dto.tracking.BookTrackingHistoryDto
import model.entity.*
import model.enums.BookStatus
import model.table.BookTrackings
import model.table.BookTrackingHistories
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import mapper.toDto
import java.time.LocalDate

class BookTrackingRepositoryImpl : BookTrackingRepository {

    override fun create(bookId: Int, status: BookStatus): BookTrackingEntity = transaction {
        val entity = BookTrackingEntity.new {
            this.bookId = bookId
            this.status = status
            borrowedAt = null
            dueDate = null
            borrowedBy = null
            reservedBy = null
            reservedUntil = null
            isDeleted = false
            updatedAt = LocalDate.now()
        }
        addHistory(entity, status, "created")
        entity
    }

    override fun findByBookId(bookId: Int): BookTrackingEntity? = transaction {
        BookTrackingEntity.find {
            (BookTrackings.bookId eq bookId) and (BookTrackings.isDeleted eq false)
        }.firstOrNull()
            ?: BookTrackingEntity.find { BookTrackings.bookId eq bookId }.firstOrNull()
    }

    override fun listAvailable(): List<BookTrackingEntity> = transaction {
        BookTrackingEntity.find {
            (BookTrackings.status eq BookStatus.AVAILABLE) and (BookTrackings.isDeleted eq false)
        }.toList()
    }

    override fun updateStatus(bookId: Int, req: UpdateBookStatusRequest): BookTrackingEntity? = transaction {
        val entity = findByBookId(bookId) ?: return@transaction null
        val now = Instant.now()

        // Логика смены статуса
        entity.status = req.status
        when (req.status) {
            BookStatus.BORROWED -> {
                entity.borrowedAt = LocalDate.now()
                entity.dueDate = req.dueDate
                entity.borrowedBy = req.borrowedBy
                entity.reservedBy = null
                entity.reservedUntil = null
            }
            BookStatus.AVAILABLE -> {
                entity.borrowedAt = null
                entity.dueDate = null
                entity.borrowedBy = null
            }
            BookStatus.RESERVED -> {
                // игнор, резерв через отдельный метод
            }
            else -> { /* MAINTENANCE, LOST */ }
        }
        entity.updatedAt = LocalDate.now()
        addHistory(entity, req.status, "status update")
        entity
    }

    override fun reserve(bookId: Int, req: ReserveBookRequest): BookTrackingEntity? = transaction {
        val entity = findByBookId(bookId) ?: return@transaction null
        if (entity.isDeleted) return@transaction null
        if (entity.status != BookStatus.AVAILABLE) return@transaction null

        entity.status = BookStatus.RESERVED
        entity.reservedBy = req.userId
        entity.reservedUntil = req.reservedUntil
        entity.updatedAt = LocalDate.now()
        addHistory(entity, BookStatus.RESERVED, "reserved by ${req.userId}")
        entity
    }

    override fun softDelete(bookId: Int): Boolean = transaction {
        val entity = findByBookId(bookId) ?: return@transaction false
        if (entity.isDeleted) return@transaction false
        entity.isDeleted = true
        entity.updatedAt = LocalDate.now()
        addHistory(entity, entity.status, "soft delete")
        true
    }

    override fun getHistory(bookId: Int): List<BookTrackingHistoryDto> = transaction {
        val tracking = findByBookId(bookId) ?: return@transaction emptyList()
        BookTrackingHistoryEntity.find { BookTrackingHistories.tracking eq tracking.id }
            .orderBy(BookTrackingHistories.changedAt to SortOrder.ASC)
            .map { it.toDto() }
    }

    private fun addHistory(tracking: BookTrackingEntity, status: BookStatus, comment: String?) {
        BookTrackingHistoryEntity.new {
            this.tracking = tracking
            this.status = status
            this.changedAt = LocalDate.now()
            this.comment = comment
        }
    }
}