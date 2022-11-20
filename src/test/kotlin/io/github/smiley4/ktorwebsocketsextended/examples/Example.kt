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
        }

        routing {
            route("ticket") {
                authenticate("auth-ws-ticket") {
                    webSocketTicket {
                        mapOf("userId" to (it.principal<UserIdPrincipal>()?.name ?: "?"))
                    }
                }
            }
            webSocketExt("test/{name}", authenticate = true) {
                provideTicket {
                    it.parameters["ticket"]!!
                }
                onConnect { call, data ->
                    data["username"] = call.parameters["name"]!!
                    println("Opening WebSocket-Connection! $data")
                }
                onOpen { connection ->
                    val name = connection.getData<String>("username")
                    println("Opening connection ${connection.getId()} $name")
                }
                onClose { connection ->
                    val name = connection.getData<String>("username")
                    println("Closing connection ${connection.getId()} $name")
                }
                binary {
                    onEach { connection, message ->
                        val name = connection.getData<String>("username")
                        println("Received binary-message on ${connection.getId()} $name (size = ${message.size})")
                    }
                }
                text {
                    onEach { connection, message ->
                        val name = connection.getData<String>("username")
                        println("Received text-message on ${connection.getId()}  $name: $message")
                    }
                }
            }
        }

    }.start(wait = true)
}
