package io.github.smiley4.ktorwebsocketsextended.routingconfig

import io.github.smiley4.ktorwebsocketsextended.session.WebSocketConnection

typealias BinaryOnEachHandler = suspend (connection: WebSocketConnection, message: ByteArray) -> Unit

class WebsocketExtendedBinaryConfig {

    var onEachHandler: BinaryOnEachHandler? = null

    fun onEach(handler: BinaryOnEachHandler) {
        this.onEachHandler = handler
    }

}