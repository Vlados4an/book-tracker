package ru.clevertec.config

import io.ktor.server.config.*

data class KafkaConfig(
    val bootstrapServers: String,
    val consumer: ConsumerConfig,
    val producer: ProducerConfig,
    val topics: Topics
) {
    data class ConsumerConfig(
        val groupId: String,
        val clientId: String,
        val autoOffsetReset: String,
        val enableAutoCommit: Boolean
    )
    data class ProducerConfig(
        val clientId: String,
        val acks: String,
        val retries: Int
    )

    data class Topics(
        val bookEvents: String,
        val authorEvents: String,
        val notificationEvents: String
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
        producer = KafkaConfig.ProducerConfig(
            clientId = property("kafka.producer.clientId").getString(),
            acks = property("kafka.producer.acks").getString(),
            retries = property("kafka.producer.retries").getString().toInt(),
        ),
        topics = KafkaConfig.Topics(
            bookEvents = kafka.property("topics.bookEvents").getString(),
            authorEvents = kafka.property("topics.authorEvents").getString(),
            notificationEvents = kafka.property("topics.notificationEvents").getString(),
        )
    )
}