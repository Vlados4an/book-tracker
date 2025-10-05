package ru.clevertec.kafka.consumer

interface KafkaProducerService {
    suspend fun <T : Any> sendEvent(topic: String, key: String, event: T): Boolean
}