package io.github.smiley4.ktorwebsocketsextended.session

data class WebSocketMessage(
    val messageIdentifier: String,
    val data: Map<String, Any?>,
    val connectionId: Int
)