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
     * How long a single ticket is valid (default = 30 seconds)
     */
    var ticketTTL: Duration = 30.seconds

}