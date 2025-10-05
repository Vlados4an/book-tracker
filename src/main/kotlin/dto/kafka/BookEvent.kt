package ru.clevertec.dto.kafka

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class BookEvent {
    abstract val bookId: Int
    abstract val timestamp: Long

    @Serializable
    @SerialName("BOOK_CREATED")
    data class Created(
        override val bookId: Int,
        override val timestamp: Long = System.currentTimeMillis(),
        val isbn: String,
        val title: String,
        val authorId: Int
    ) : BookEvent()

    @Serializable
    @SerialName("BOOK_UPDATED")
    data class Updated(
        override val bookId: Int,
        override val timestamp: Long = System.currentTimeMillis(),
        val changes: Map<String, String>
    ) : BookEvent()

    @Serializable
    @SerialName("BOOK_DELETED")
    data class Deleted(
        override val bookId: Int,
        override val timestamp: Long = System.currentTimeMillis()
    ) : BookEvent()
}
