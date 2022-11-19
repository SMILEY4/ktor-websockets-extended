package io.github.smiley4.ktorwebsocketsextended.session

import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.send
import mu.KotlinLogging
import java.util.Collections

/**
 * Handler for active websocket-connections
 */
class WebSocketConnectionHandler {

    private val logger = KotlinLogging.logger(WebSocketConnectionHandler::class.java.name)
    private val connections = Collections.synchronizedMap(HashMap<Long, WebSocketConnection>())

    /**
     * Add a new connection
     * @param session the new websocket-session
     * @return the connection
     */
    fun open(session: DefaultWebSocketSession): WebSocketConnection {
        val connection = WebSocketConnection(session)
        connections[connection.getId()] = connection
        logger.debug("Added new websocket-connection with id ${connection.getId()}")
        return connection
    }

    /**
     * Removes the given connection
     * @param connection the connection to close
     */
    fun close(connection: WebSocketConnection) {
        val removed = connections.remove(connection.getId()) != null
        if (removed) {
            logger.debug("Removed websocket-connection with id ${connection.getId()}")
        }
    }

    /**
     * @return all currently active connections
     */
    fun getAllConnections(): Set<WebSocketConnection> = connections.values.toMutableSet()

    /**
     * @return the connection with the given id (or null)
     */
    fun getConnection(connectionId: Long) = connections[connectionId]

    /**
     * Send a message to the given connection
     * @param connectionId the id of the connection to send the message to
     * @param content the content of the message
     */
    suspend fun sendTo(connectionId: Long, content: String) {
        connections[connectionId]?.getSession()?.send(content)
    }

    /**
     * Send a message to the given connection
     * @param connectionId the id of the connection to send the message to
     * @param content the content of the message
     */
    suspend fun sendTo(connectionId: Long, content: ByteArray) {
        connections[connectionId]?.getSession()?.send(content)
    }

}



