package config

import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import service.notification.NotificationScheduler

fun Application.configureScheduler() {
    val di by closestDI()
    val scheduler by di.instance<NotificationScheduler>()

    val schedulerScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    scheduler.start(schedulerScope)

    environment.monitor.subscribe(ApplicationStopping) {
        log.info("Shutting down scheduler...")
        scheduler.stop()
        schedulerScope.cancel()
    }
}