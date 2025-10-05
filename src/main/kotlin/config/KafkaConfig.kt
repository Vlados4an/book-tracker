package ru.clevertec.config

import io.ktor.server.config.*

data class KafkaConfig(
    val bootstrapServers: String,
    val producer: ProducerConfig,
    val topics: Topics
) {
    data class ProducerConfig(
        val clientId: String,
        val acks: String,
        val retries: Int
    )

    data class Topics(
        val bookEvents: String,
        val authorEvents: String
    )
}

fun ApplicationConfig.getKafkaConfig(): KafkaConfig {
    val kafka = config("kafka")
    return KafkaConfig(
        bootstrapServers = kafka.property("bootstrapServers").getString(),
        producer = KafkaConfig.ProducerConfig(
            clientId = kafka.property("producer.clientId").getString(),
            acks = kafka.property("producer.acks").getString(),
            retries = kafka.property("producer.retries").getString().toInt()
        ),
        topics = KafkaConfig.Topics(
            bookEvents = kafka.property("topics.bookEvents").getString(),
            authorEvents = kafka.property("topics.authorEvents").getString()
        )
    )
}