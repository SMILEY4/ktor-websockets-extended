package io.github.smiley4.ktorwebsocketsextended

import io.github.smiley4.ktorwebsocketsextended.auth.WebsocketTicketAuthManager
import io.ktor.server.application.ApplicationCall
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


/**
 * Main-Configuration of the "WebsocketsExtended"-Plugin
 */
class WebsocketsExtendedPluginConfig {

    /**
     * A custom [WebsocketTicketAuthManager] to use instead of the default one.
     */
    var customTicketManager: WebsocketTicketAuthManager? = null

    /**
     * How long a single ticket is valid
     */
    var ticketTTL: Duration = 30.seconds

    private var ticketDataBuilder: (call: ApplicationCall) -> Map<String, Any?> = { mapOf() }

    fun ticketData(block: (call: ApplicationCall) -> Map<String, Any?>) {
        ticketDataBuilder = block
    }

    fun getTicketDataBuilder() = ticketDataBuilder

}