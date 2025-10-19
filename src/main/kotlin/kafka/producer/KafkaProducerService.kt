package ru.clevertec.kafka.producer

interface KafkaProducerService {
    suspend fun <T : Any> sendEvent(topic: String, key: String, event: T): Boolean
}