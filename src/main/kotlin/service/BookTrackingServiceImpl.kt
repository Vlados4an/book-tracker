package ru.clevertec.service

import dto.tracking.*
import mapper.toDto
import ru.clevertec.repository.BookTrackingRepository

class BookTrackingServiceImpl(
    private val repo: BookTrackingRepository
) : BookTrackingService {

    override fun create(req: CreateBookTrackingRequest) =
        repo.create(req).toDto()

    override fun getAvailable() = repo.listAvailable().map { it.toDto() }

    override fun get(bookId: Int) = repo.findByBookId(bookId)?.toDto()

    override fun updateStatus(bookId: Int, req: UpdateBookStatusRequest) =
        repo.updateStatus(bookId, req)?.toDto()

    override fun reserve(bookId: Int, req: ReserveBookRequest) =
        repo.reserve(bookId, req)?.toDto()

    override fun delete(bookId: Int) = repo.softDelete(bookId)

    override fun history(bookId: Int) = repo.getHistory(bookId)
}