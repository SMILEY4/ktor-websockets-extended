package io.github.smiley4.ktorwebsocketsextended

import io.github.smiley4.ktorwebsocketsextended.auth.WebsocketTicketAuthManager
import io.github.smiley4.ktorwebsocketsextended.auth.WebsocketTicketAuthManagerImpl
import io.github.smiley4.ktorwebsocketsextended.session.WebSocketConnectionHandler
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.createApplicationPlugin


val WebsocketsExtended = createApplicationPlugin(name = "WebsocketsExtended", createConfiguration = ::WebsocketsExtendedPluginConfig) {
    WSExtended.initialize(
        ticketManager = pluginConfig.customTicketManager ?: WebsocketTicketAuthManagerImpl(pluginConfig.ticketTTL),
        ticketDataBuilder = pluginConfig.getTicketDataBuilder(),
        connectionHandler = WebSocketConnectionHandler()
    )
}

object WSExtended {

    private var ticketManager: WebsocketTicketAuthManager? = null
    private var ticketDataBuilder: ((call: ApplicationCall) -> Map<String, Any?>)? = null
    private var connectionHandler: WebSocketConnectionHandler? = null

    internal fun initialize(
        ticketManager: WebsocketTicketAuthManager,
        ticketDataBuilder: (call: ApplicationCall) -> Map<String, Any?>,
        connectionHandler: WebSocketConnectionHandler
    ) {
        WSExtended.ticketManager = ticketManager
        WSExtended.ticketDataBuilder = ticketDataBuilder
        WSExtended.connectionHandler = connectionHandler
    }

    fun getTicketManager(): WebsocketTicketAuthManager {
        return ticketManager ?: throw Exception("No ticket-manager. Initialize plugin first.")
    }

    fun getTicketDataBuilder(): (call: ApplicationCall) -> Map<String, Any?> {
        return ticketDataBuilder ?: throw Exception("No ticket-data-builder. Initialize plugin first.")
    }

    fun getConnectionHandler(): WebSocketConnectionHandler {
        return connectionHandler ?: throw Exception("No connection-handler. Initialize plugin first.")
    }

}
