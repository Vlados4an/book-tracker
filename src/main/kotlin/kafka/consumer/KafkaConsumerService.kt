package kafka.consumer

import kotlinx.coroutines.CoroutineScope

interface KafkaConsumerService {
    fun start(scope: CoroutineScope, topics: List<String>)
    fun stop()
}