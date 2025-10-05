package config

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import io.ktor.server.application.log
import kafka.consumer.KafkaConsumerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.clevertec.config.KafkaConfig

fun Application.configureKafka() {
    val di by closestDI()

    val kafkaConfig by di.instance<KafkaConfig>()
    val kafkaConsumer by di.instance<KafkaConsumerService>()

    val consumerScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    kafkaConsumer.start(
        scope = consumerScope,
        topics = listOf(
            kafkaConfig.topics.bookEvents,
            kafkaConfig.topics.authorEvents
        )
    )

    environment.monitor.subscribe(ApplicationStopping) {
        log.info("Shutting down Kafka...")
        kafkaConsumer.stop()
        consumerScope.cancel()
    }
}