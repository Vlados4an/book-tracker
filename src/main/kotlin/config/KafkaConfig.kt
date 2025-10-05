package ru.clevertec.config

import io.ktor.server.config.*

data class KafkaConfig(
    val bootstrapServers: String,
    val consumer: ConsumerConfig,
    val topics: Topics
) {
    data class ConsumerConfig(
        val groupId: String,
        val clientId: String,
        val autoOffsetReset: String,
        val enableAutoCommit: Boolean
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
        consumer = KafkaConfig.ConsumerConfig(
            groupId = kafka.property("consumer.groupId").getString(),
            clientId = kafka.property("consumer.clientId").getString(),
            autoOffsetReset = kafka.property("consumer.autoOffsetReset").getString(),
            enableAutoCommit = kafka.property("consumer.enableAutoCommit").getString().toBoolean()
        ),
        topics = KafkaConfig.Topics(
            bookEvents = kafka.property("topics.bookEvents").getString(),
            authorEvents = kafka.property("topics.authorEvents").getString()
        )
    )
}