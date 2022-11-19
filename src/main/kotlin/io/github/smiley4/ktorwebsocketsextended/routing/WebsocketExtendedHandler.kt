package io.github.smiley4.ktorwebsocketsextended.routing

import io.github.smiley4.ktorwebsocketsextended.WSExtended
import io.github.smiley4.ktorwebsocketsextended.routingconfig.WebsocketExtendedRouteConfig
import io.github.smiley4.ktorwebsocketsextended.session.WebSocketConnection
import io.github.smiley4.ktorwebsocketsextended.session.WebSocketConnectionHandler
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import mu.KotlinLogging

class WebsocketExtendedHandler(
    private val config: WebsocketExtendedRouteConfig,
    private val connectionHandler: WebSocketConnectionHandler
) {

    private val logger = KotlinLogging.logger(WebsocketExtendedHandler::class.java.name)

    /**
     * handle the websocket-connection before any message
     */
    suspend fun handleBefore(call: ApplicationCall, authenticate: Boolean) {
        if (!authenticate) {
            config.onConnectHandler(call, mapOf())
        } else {
            val ticket = config.tickerProvider(call)
            val ticketManager = WSExtended.getTicketManager()
            if (ticket == null || !ticketManager.validateAndConsumeTicket(ticket)) {
                call.respond(HttpStatusCode.Unauthorized)
            } else {
                config.onConnectHandler(call, ticketManager.extractData(ticket))
            }
        }
    }

    /**
     * handle the open websocket-connection
     */
    suspend fun handleSession(session: DefaultWebSocketServerSession) {
        val connection = connectionHandler.open(session)
        try {
            for (frame in session.incoming) {
                handleFrame(connection, frame)
            }
        } finally {
            config.onCloseHandler(connection)
            connectionHandler.close(connection)
        }
    }

    /**
     * handle the given message/frame from the given connection
     */
    private fun handleFrame(connection: WebSocketConnection, frame: Frame) {
        when (frame) {
            is Frame.Text -> {
                config.textConfig?.onEachHandler?.let {
                    it(connection, frame.readText())
                }
            }
            is Frame.Binary -> {
                config.binaryConfig?.onEachHandler?.let {
                    it(connection, frame.readBytes())
                }
            }
            else -> logger.warn("Unhandled websocket frame-type: ${frame.frameType}")
        }
    }

}