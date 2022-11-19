package io.github.smiley4.ktorwebsocketsextended.routingconfig

import io.github.smiley4.ktorwebsocketsextended.session.WebSocketConnection

class WebsocketExtendedBinaryConfig {

    var onEachHandler: ((connection: WebSocketConnection, message: ByteArray) -> Unit)? = null

    fun onEach(handler: (connection: WebSocketConnection, message: ByteArray) -> Unit) {
        this.onEachHandler = handler
    }



}