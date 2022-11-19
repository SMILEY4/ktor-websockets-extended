package io.github.smiley4.ktorwebsocketsextended.routingconfig

import io.github.smiley4.ktorwebsocketsextended.session.WebSocketConnection

class WebsocketExtendedTextConfig {

    var onEachHandler: ((connection: WebSocketConnection, message: String) -> Unit)? = null

    fun onEach(handler: (connection: WebSocketConnection, message: String) -> Unit) {
        this.onEachHandler = handler
    }



}