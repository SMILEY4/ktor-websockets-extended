package io.github.smiley4.ktorwebsocketsextended.examples

import io.github.smiley4.ktorwebsocketsextended.WebsocketsExtended
import io.github.smiley4.ktorwebsocketsextended.routing.webSocketExt
import io.github.smiley4.ktorwebsocketsextended.routing.webSocketTicket
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.auth.principal
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import java.time.Duration
import kotlin.time.Duration.Companion.seconds

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {

        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(15)
            timeout = Duration.ofSeconds(15)
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }
        install(Authentication) {
            basic("auth-ws-ticket") {
                realm = "Access to WebSocket-Tickets"
                validate { credentials ->
                    if (credentials.name == "user" && credentials.password == "pass") {
                        UserIdPrincipal(credentials.name)
                    } else {
                        null
                    }
                }
            }
        }
        install(WebsocketsExtended) {
            ticketTTL = 30.seconds
            ticketData { call ->
                mapOf(
                    "userId" to (call.principal<UserIdPrincipal>()?.name ?: "?")
                )
            }
        }

        routing {
            route("ticket") {
                authenticate("auth-ws-ticket") {
                    webSocketTicket()
                }
            }
            webSocketExt("test", authenticate = true) {
                provideTicket {
                    it.parameters["ticket"]!!
                }
                onConnect { _, data ->
                    data["x"] = "hello"
                    println("Opening WebSocket-Connection! $data")
                }
                onOpen { connection ->
                    connection.getData<String>("x")
                }
                onClose { connection ->
                    println("Closing connection ${connection.getId()}")
                }
                binary {
                    onEach { connection, message ->
                        println("Received binary-message on ${connection.getId()} (size = ${message.size})")
                    }
                }
                text {
                    onEach { connection, message ->
                        println("Received text-message on ${connection.getId()}: $message")
                    }
                }
            }
        }

    }.start(wait = true)
}
