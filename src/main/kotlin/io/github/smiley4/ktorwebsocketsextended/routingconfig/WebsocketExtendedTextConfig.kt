package io.github.smiley4.ktorwebsocketsextended.routingconfig

import io.github.smiley4.ktorwebsocketsextended.session.WebSocketConnection

typealias TextOnEachHandler = suspend (connection: WebSocketConnection, message: String) -> Unit

class WebsocketExtendedTextConfig {

    var onEachHandler: TextOnEachHandler? = null

    fun onEach(handler: TextOnEachHandler) {
        this.onEachHandler = handler
    }

}