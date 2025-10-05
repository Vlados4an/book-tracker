package ru.clevertec.service

import dto.tracking.*
import dto.tracking.BookTrackingHistoryDto

interface BookTrackingService {
    fun create(req: CreateBookTrackingRequest): BookTrackingDto
    fun getAvailable(): List<BookTrackingDto>
    fun get(bookId: Int): BookTrackingDto?
    fun updateStatus(bookId: Int, req: UpdateBookStatusRequest): BookTrackingDto?
    fun reserve(bookId: Int, req: ReserveBookRequest): BookTrackingDto?
    fun delete(bookId: Int): Boolean
    fun history(bookId: Int): List<BookTrackingHistoryDto>
}