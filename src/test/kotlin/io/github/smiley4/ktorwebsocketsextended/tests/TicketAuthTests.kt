package io.github.smiley4.ktorwebsocketsextended.tests

import io.github.smiley4.ktorwebsocketsextended.auth.WebsocketTicketAuthManagerImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

class TicketAuthTests : StringSpec({

    "generate valid ticket" {
        val ticketManager = WebsocketTicketAuthManagerImpl(24.hours)
        val ticket = ticketManager.generateTicket(
            mapOf(
                "username" to "testuser",
                "testvalue" to "something",
                "someNumber" to 42
            )
        )
        ticketManager.extractData(ticket).also {
            it.size shouldBe 5
            it["username"] shouldBe "testuser"
            it["testvalue"] shouldBe "something"
            it["someNumber"] shouldBe 42
            it[WebsocketTicketAuthManagerImpl.KEY_TOKEN].shouldNotBeNull()
            it[WebsocketTicketAuthManagerImpl.KEY_VALID_UNTIL].shouldNotBeNull()
        }
    }

    "dont accept token as additional data" {
        val ticketManager = WebsocketTicketAuthManagerImpl(24.hours)
        shouldThrow<Exception> {
            ticketManager.generateTicket(
                mapOf(
                    "username" to "testuser",
                    WebsocketTicketAuthManagerImpl.KEY_TOKEN to "invalid"
                )
            )
        }
    }

    "dont accept valid-until as additional data" {
        val ticketManager = WebsocketTicketAuthManagerImpl(24.hours)
        shouldThrow<Exception> {
            ticketManager.generateTicket(
                mapOf(
                    "username" to "testuser",
                    WebsocketTicketAuthManagerImpl.KEY_VALID_UNTIL to "invalid"
                )
            )
        }
    }

    "validate valid ticket" {
        val ticketManager = WebsocketTicketAuthManagerImpl(24.hours)
        val ticket = ticketManager.generateTicket(mapOf("username" to "testuser"))
        ticketManager.validateAndConsumeTicket(ticket) shouldBe true
    }

    "validate corrupted ticket" {
        val ticketManager = WebsocketTicketAuthManagerImpl(24.hours)
        ticketManager.validateAndConsumeTicket("corrupted") shouldBe false
    }

    "validate ticket timout" {
        val ticketManager = WebsocketTicketAuthManagerImpl(1.milliseconds)
        val ticket = ticketManager.generateTicket(mapOf("username" to "testuser"))
        withContext(Dispatchers.IO) { Thread.sleep(100) }
        ticketManager.validateAndConsumeTicket(ticket) shouldBe false
    }

    "validate valid ticket twice" {
        val ticketManager = WebsocketTicketAuthManagerImpl(24.hours)
        val ticket = ticketManager.generateTicket(mapOf("username" to "testuser"))
        ticketManager.validateAndConsumeTicket(ticket) shouldBe true
        ticketManager.validateAndConsumeTicket(ticket) shouldBe false
        ticketManager.validateAndConsumeTicket(ticket) shouldBe false
    }

})