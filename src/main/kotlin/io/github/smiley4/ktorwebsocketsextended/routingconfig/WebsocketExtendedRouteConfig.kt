package io.github.smiley4.ktorwebsocketsextended.routingconfig

import io.github.smiley4.ktorwebsocketsextended.session.WebSocketConnection
import io.ktor.server.application.ApplicationCall


typealias TicketProvider = (call: ApplicationCall) -> String?

typealias OnConnectHandler = suspend (call: ApplicationCall, data: MutableMap<String, Any?>) -> Unit

typealias OnOpenHandler = suspend (connection: WebSocketConnection) -> Unit

typealias OnCloseHandler = suspend (connection: WebSocketConnection) -> Unit


class WebsocketExtendedRouteConfig {

    var tickerProvider: TicketProvider = { null }

    fun provideTicket(tickerProvider: TicketProvider) {
        this.tickerProvider = tickerProvider
    }


    var onConnectHandler: OnConnectHandler = { _, _ -> }

    /**
     * called on a new websocket-request before a "true" connection is established
     * @param handler the callback - takes the request as [ApplicationCall] and the additional data extracted from the auth ticket
     */
    fun onConnect(handler: OnConnectHandler) {
        this.onConnectHandler = handler
    }


    var onOpenHandler: OnOpenHandler = { }

    fun onOpen(handler: OnOpenHandler) {
        this.onOpenHandler = handler
    }


    var onCloseHandler: OnCloseHandler = { }

    fun onClose(handler: OnCloseHandler) {
        this.onCloseHandler = handler
    }


    var binaryConfig: WebsocketExtendedBinaryConfig? = null

    fun binary(config: WebsocketExtendedBinaryConfig.() -> Unit) {
        binaryConfig = WebsocketExtendedBinaryConfig().apply(config)
    }


    var textConfig: WebsocketExtendedTextConfig? = null

    fun text(config: WebsocketExtendedTextConfig.() -> Unit) {
        textConfig = WebsocketExtendedTextConfig().apply(config)
    }


}