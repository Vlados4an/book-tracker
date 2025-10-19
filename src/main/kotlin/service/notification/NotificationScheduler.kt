package service.notification

import dto.kafka.NotificationEvent
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import ru.clevertec.config.KafkaConfig
import ru.clevertec.kafka.producer.KafkaProducerService
import ru.clevertec.repository.BookTrackingRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.hours

class NotificationScheduler(
    private val kafkaProducerService: KafkaProducerService,
    private val bookTrackingRepository: BookTrackingRepository,
    private val kafkaConfig: KafkaConfig
) {
    private val logger = LoggerFactory.getLogger(NotificationScheduler::class.java)
    private var schedulerJob: Job? = null

    fun start(scope: CoroutineScope) {
        logger.info("Starting notification scheduler...")
        schedulerJob = scope.launch {
            while (isActive) {
                try {
                    checkAndSendNotifications()
                } catch (e: Exception) {
                    logger.error("Error in notification scheduler", e)
                }
                delay(24.hours)
            }
        }
    }

    fun stop() {
        logger.info("Stopping notification scheduler...")
        schedulerJob?.cancel()
    }

    private suspend fun checkAndSendNotifications() {
        logger.info("Checking for books with upcoming due dates...")

        val booksToNotify = bookTrackingRepository.findReservedBooksWithUpcomingDueDate()

        logger.info("Found ${booksToNotify.size} books with upcoming reservation expiry")

        booksToNotify.forEach { tracking ->
            val reservedUntil = tracking.reservedUntil
            val reservedBy = tracking.reservedBy

            if (reservedUntil != null && reservedBy != null) {
                val daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), reservedUntil)

                val notification = NotificationEvent(
                    bookId = tracking.bookId,
                    reservedBy = reservedBy,
                    reservedUntil = reservedUntil,
                    message = "Your reservation for book ${tracking.bookId} expires in $daysRemaining day(s)"
                )

                val sent = kafkaProducerService.sendEvent(
                    topic = kafkaConfig.topics.notificationEvents,
                    key = tracking.bookId.toString(),
                    event = notification
                )

                if (sent) {
                    logger.info("Notification sent for book ${tracking.bookId} to user $reservedBy")
                } else {
                    logger.error("Failed to send notification for book ${tracking.bookId}")
                }
            }
        }
    }
}