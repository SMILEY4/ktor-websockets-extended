# Ktor WebSockets-Extended (WIP)

This library provides additional functionalities for (serverside) WebSockets.

## Features

- ticket-based websocket-authentication
  - get short-lived one-time ticket from authenticated endpoint
  - provide ticket for websocket-request e.g. as query-parameter
- perform logic before establishing proper websocket-connection, e.g. to validate and reject request
- simplified receiving of messages using callback-functions
- session-manager handles and knows all open connections, use to send messages

## Installation

todo

## Example

```kotlin
install(WebSockets) { /*...*/ }
install(Authentication) { /*e.g. basic auth...*/ }
install(WebsocketsExtended) {
  ticketTTL = 30.seconds
}

routing {
    authenticate {
        route("/ticket") {
            // authenticated route '/ticket' to fetch new ticket, add info 'username' to ticket
            webSocketTicket { call ->
                  mapOf("username" to (call.principal<UserIdPrincipal>()?.name ?: "?"))
            }
        }
    }
    webSocketExt("/websocket", authenticate = true) {
        // get the ticket from the call
        provideTicket { call -> call.parameters["ticket"]!! }
        // called before "real" websocket connection
        onConnect { call, data ->
            val username = data["username"] // from ticket
            data["something"] = "else" // add more custom data
        }
        // called when the connection is opened, before any message
        onOpen { connection -> 
          val username = connection.getData<String>("username")
          val something = connection.getData<String>("something")
          // ...
        }
        // called when the connection is closed (for any reason)
        onClose { connection -> /*...*/ }
        // section for incoming text-messages
        text {
            // called for each incoming text-message
            onEach { connection, message -> /*...*/ }
        }
        // section for incoming binary-messages
        binary {
            // called for each incoming binary-message
            onEach { connection, message -> /*...*/ }
        }
    }
}
```