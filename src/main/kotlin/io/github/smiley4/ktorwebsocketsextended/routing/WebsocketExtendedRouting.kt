package io.github.smiley4.ktorwebsocketsextended.routing

import io.github.smiley4.ktorwebsocketsextended.WSExtended
import io.github.smiley4.ktorwebsocketsextended.routingconfig.WebsocketExtendedRouteConfig
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.RouteSelector
import io.ktor.server.routing.RouteSelectorEvaluation
import io.ktor.server.routing.RoutingResolveContext
import io.ktor.server.routing.get
import io.ktor.server.websocket.webSocket
import io.ktor.util.pipeline.PipelineContext

/**
 * Creates the route providing tickets for authenticating websocket-connections.
 */
fun Route.webSocketTicket() {
    get {
        val ticketManager = WSExtended.getTicketManager()
        val ticketDataBuilder = WSExtended.getTicketDataBuilder()
        val additionalData = ticketDataBuilder(call)
        call.respondText(ticketManager.generateTicket(additionalData))
    }
}

/**
 * See [webSocket]
 */
fun Route.webSocketExt(protocol: String? = null, authenticate: Boolean = false, config: WebsocketExtendedRouteConfig.() -> Unit) {
    val handler = WebsocketExtendedHandler(WebsocketExtendedRouteConfig().apply(config), WSExtended.getConnectionHandler())
    interceptWebsocketRequest(
        interceptor = {
            handler.handleBefore(call, authenticate)
        },
        callback = {
            webSocket(protocol) {
                handler.handleSession(this)
            }
        }
    )
}

/**
 * See [webSocket]
 */
fun Route.webSocketExt(
    path: String,
    protocol: String? = null,
    authenticate: Boolean = false,
    config: WebsocketExtendedRouteConfig.() -> Unit
) {
    val handler = WebsocketExtendedHandler(WebsocketExtendedRouteConfig().apply(config), WSExtended.getConnectionHandler())
    interceptWebsocketRequest(
        interceptor = {
            handler.handleBefore(call, authenticate)
        },
        callback = {
            webSocket(path, protocol) {
                handler.handleSession(this)
            }
        }
    )
}

/**
 * Intercept a websocket-request before a proper connection is established
 */
private fun Route.interceptWebsocketRequest(
    interceptor: suspend PipelineContext<Unit, ApplicationCall>.() -> Unit,
    callback: Route.() -> Unit
): Route {
    val route = createChild(object : RouteSelector() {
        override fun evaluate(context: RoutingResolveContext, segmentIndex: Int) = RouteSelectorEvaluation.Constant
    })
    route.intercept(ApplicationCallPipeline.Plugins) {
        interceptor()
    }
    callback(route)
    return route
}
