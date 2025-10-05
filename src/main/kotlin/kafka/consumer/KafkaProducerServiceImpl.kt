package kafka.consumer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import ru.clevertec.config.KafkaConfig
import ru.clevertec.kafka.consumer.KafkaProducerService
import java.util.Properties
import java.util.concurrent.TimeUnit

class KafkaProducerServiceImpl(private val config: KafkaConfig) : KafkaProducerService {

    private val logger = LoggerFactory.getLogger(KafkaProducerServiceImpl::class.java)

    private val producer: KafkaProducer<String, String> by lazy {
        val props = Properties().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers)
            put(ProducerConfig.CLIENT_ID_CONFIG, config.producer.clientId)
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            put(ProducerConfig.ACKS_CONFIG, config.producer.acks)
            put(ProducerConfig.RETRIES_CONFIG, config.producer.retries)
            put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true)
            put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5)
            put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy")
            put(ProducerConfig.LINGER_MS_CONFIG, 10)
            put(ProducerConfig.BATCH_SIZE_CONFIG, 32 * 1024)
        }
        KafkaProducer(props)
    }

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    override suspend fun <T : Any> sendEvent(topic: String, key: String, event: T): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val eventJson = json.encodeToString(serializer(event::class.java), event)
                val record = ProducerRecord(topic, key, eventJson)

                record.headers().add("event-type", event::class.simpleName?.toByteArray())
                record.headers().add("timestamp", System.currentTimeMillis().toString().toByteArray())

                val metadata = producer.send(record).get(5, TimeUnit.SECONDS)

                logger.info(
                    "Event sent successfully: topic=${metadata.topic()}, " +
                            "partition=${metadata.partition()}, offset=${metadata.offset()}"
                )
                true
            } catch (e: Exception) {
                logger.error("Failed to send event to topic $topic", e)
                false
            }
        }
    }
}