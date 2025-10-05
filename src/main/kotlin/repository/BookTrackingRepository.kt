package ru.clevertec.repository

import dto.tracking.ReserveBookRequest
import dto.tracking.UpdateBookStatusRequest
import model.entity.BookTrackingEntity
import dto.tracking.BookTrackingHistoryDto
import model.enums.BookStatus

interface BookTrackingRepository {
    fun create(bookId: Int, status: BookStatus): BookTrackingEntity
    fun findByBookId(bookId: Int): BookTrackingEntity?
    fun listAvailable(): List<BookTrackingEntity>
    fun updateStatus(bookId: Int, req: UpdateBookStatusRequest): BookTrackingEntity?
    fun reserve(bookId: Int, req: ReserveBookRequest): BookTrackingEntity?
    fun softDelete(bookId: Int): Boolean
    fun getHistory(bookId: Int): List<BookTrackingHistoryDto>
}