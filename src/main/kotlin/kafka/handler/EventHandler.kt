package ru.clevertec.kafka.handler

interface EventHandler<T> {
    suspend fun handle(eventJson: String)
}