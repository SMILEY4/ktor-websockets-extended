package io.github.smiley4.ktorwebsocketsextended.auth

/**
 * Handles tickets used for authenticating websocket requests.
 * Authentication Process:
 * - client calls (authenticated) rest-endpoint to obtain authorization ticket
 * - server generates this ticket ([WebsocketTicketAuthManager.generateTicket])
 * - server stores this ticket and returns it to the client
 * - the client opens the websocket connection and sends the ticket as part of the url (e.g.. as query-parameter)
 * - the server checks if the ticket is known, not already used and still valid ([WebsocketTicketAuthManager.validateTicket])
 * see https://devcenter.heroku.com/articles/websocket-security for more info about the concept
 */
interface WebsocketTicketAuthManager {

    /**
     * generate a new ticket
     */
    fun generateTicket(additionalData: Map<String, Any?>): String

    /**
     * validate the given ticket
     */
    fun validateAndConsumeTicket(ticket: String): Boolean

    /**
     * Extract the additional data from the given valid ticket
     */
    fun extractData(ticket: String): Map<String,Any?>

}