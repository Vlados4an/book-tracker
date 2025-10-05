package kafka.consumer

import ru.clevertec.kafka.handler.EventHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import ru.clevertec.config.KafkaConfig
import java.time.Duration
import java.util.*

class KafkaConsumerServiceImpl(
    private val config: KafkaConfig,
    private val eventHandlers: Map<String, EventHandler<*>>
) : KafkaConsumerService {

    private val logger = LoggerFactory.getLogger(KafkaConsumerServiceImpl::class.java)

    private val consumer: KafkaConsumer<String, String> by lazy {
        val props = Properties().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers)
            put(ConsumerConfig.GROUP_ID_CONFIG, config.consumer.groupId)
            put(ConsumerConfig.CLIENT_ID_CONFIG, config.consumer.clientId)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.consumer.autoOffsetReset)
            put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, config.consumer.enableAutoCommit)
            put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500)
        }
        KafkaConsumer(props)
    }

    @Volatile
    private var isRunning = false

    override fun start(scope: CoroutineScope, topics: List<String>) {
        if (isRunning) return

        isRunning = true
        scope.launch(Dispatchers.IO) {
            try {
                consumer.subscribe(topics)
                logger.info("Subscribed to topics: $topics")

                while (isRunning) {
                    val records = consumer.poll(Duration.ofMillis(1000))

                    for (record in records) {
                        try {
                            val handler = eventHandlers[record.topic()]
                            if (handler != null) {
                                withContext(Dispatchers.Default) {
                                    handler.handle(record.value())
                                }

                                if (!config.consumer.enableAutoCommit) {
                                    consumer.commitSync()
                                }
                            }
                        } catch (e: Exception) {
                            logger.error("Error processing record", e)
                        }
                    }
                }
            } finally {
                consumer.close()
                logger.info("Consumer closed")
            }
        }
    }

    override fun stop() {
        isRunning = false
    }
}

