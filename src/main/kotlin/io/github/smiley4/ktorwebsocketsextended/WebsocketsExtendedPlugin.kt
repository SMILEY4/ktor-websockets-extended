package io.github.smiley4.ktorwebsocketsextended

import io.github.smiley4.ktorwebsocketsextended.auth.WebsocketTicketAuthManager
import io.github.smiley4.ktorwebsocketsextended.auth.WebsocketTicketAuthManagerImpl
import io.github.smiley4.ktorwebsocketsextended.session.WebSocketConnectionHandler
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.createApplicationPlugin


val WebsocketsExtended = createApplicationPlugin(name = "WebsocketsExtended", createConfiguration = ::WebsocketsExtendedPluginConfig) {
    WSExtended.initialize(
        ticketManager = pluginConfig.customTicketManager ?: WebsocketTicketAuthManagerImpl(pluginConfig.ticketTTL),
        connectionHandler = WebSocketConnectionHandler()
    )
}

object WSExtended {

    private var ticketManager: WebsocketTicketAuthManager? = null
    private var connectionHandler: WebSocketConnectionHandler? = null

    internal fun initialize(
        ticketManager: WebsocketTicketAuthManager,
        connectionHandler: WebSocketConnectionHandler
    ) {
        WSExtended.ticketManager = ticketManager
        WSExtended.connectionHandler = connectionHandler
    }

    fun getTicketManager(): WebsocketTicketAuthManager {
        return ticketManager ?: throw Exception("No ticket-manager. Initialize plugin first.")
    }

    fun getConnectionHandler(): WebSocketConnectionHandler {
        return connectionHandler ?: throw Exception("No connection-handler. Initialize plugin first.")
    }

}
