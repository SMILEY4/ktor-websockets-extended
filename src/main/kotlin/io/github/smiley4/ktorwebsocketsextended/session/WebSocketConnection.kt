package io.github.smiley4.ktorwebsocketsextended.session

import io.ktor.websocket.DefaultWebSocketSession
import java.util.concurrent.atomic.AtomicLong

/**
 * A single websocket connection
 * @param session the websocket session
 */
class WebSocketConnection(private val session: DefaultWebSocketSession) {

    private companion object {
        var lastId = AtomicLong(0)
    }

    /**
     * The id of this connection (unique among the current connections).
     */
    private val id: Long = lastId.getAndIncrement()

    /**
     * @return the id of this connection.
     */
    fun getId() = id

    /**
     * @return the session associated with this connection
     */
    fun getSession() = session

}