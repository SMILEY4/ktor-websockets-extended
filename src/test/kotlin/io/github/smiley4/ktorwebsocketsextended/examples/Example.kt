package io.github.smiley4.ktorwebsocketsextended.examples

import io.github.smiley4.ktorwebsocketsextended.WebsocketsExtended
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.send
import java.time.Duration

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {

        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(15)
            timeout = Duration.ofSeconds(15)
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }
        install(WebsocketsExtended)

        routing {
            route("ws") {
                webSocket("test") {
                    this.send("Hello World!")
                }
            }
        }

    }.start(wait = true)
}
