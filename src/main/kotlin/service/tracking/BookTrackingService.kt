package ru.clevertec.service.tracking

import dto.tracking.BookTrackingDto
import dto.tracking.BookTrackingHistoryDto
import dto.tracking.CreateBookTrackingRequest
import dto.tracking.ReserveBookRequest
import dto.tracking.UpdateBookStatusRequest

interface BookTrackingService {
    fun create(req: CreateBookTrackingRequest): BookTrackingDto
    fun getAvailable(): List<BookTrackingDto>
    fun get(bookId: Int): BookTrackingDto?
    fun updateStatus(bookId: Int, req: UpdateBookStatusRequest): BookTrackingDto?
    fun reserve(bookId: Int, req: ReserveBookRequest): BookTrackingDto?
    fun delete(bookId: Int): Boolean
    fun history(bookId: Int): List<BookTrackingHistoryDto>
}