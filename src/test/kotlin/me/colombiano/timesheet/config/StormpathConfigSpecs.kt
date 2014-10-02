package me.colombiano.timesheet.config

import org.spek.Spek
import kotlin.test.assertEquals
import org.mockito.Mock
import org.mockito.Mockito.*
import me.colombiano.timesheet.environment.Environment
import kotlin.test.expect
import kotlin.test.fail
import kotlin.test.fails
import me.colombiano.timesheet.security

class StormpathConfigSpecs : Spek() {{

    given("a stormpath config") {
        val expectedApplicationRestUrl = "https://api.stormpath.com/v1/applications/123"
        val environmentMock = mock(javaClass<Environment>())
        `when`(environmentMock?.stormpathApplicationId())?.thenReturn("123")
        `when`(environmentMock?.stormpathApikeyId())?.thenReturn("api_key_id")
        `when`(environmentMock?.stormpathApikeySecret())?.thenReturn("api_key_secret")

        val stormpathConfig = security.StormpathConfig()
        stormpathConfig.environment(environmentMock)

        on("calling stormpathApplicationRestUrl") {
            val stormpathApplicationRestUrl = stormpathConfig.stormpathApplicationRestUrl()

            it("should return url") {
                assertEquals(expectedApplicationRestUrl, stormpathApplicationRestUrl)
            }
        }

        on("calling stormpathApikeyId") {
            val stormpathApikeyId = stormpathConfig.stormpathApikeyId()

            it("should return 'api_key_id'") {
                assertEquals("api_key_id", stormpathApikeyId)
            }
        }

        on("calling stormpathApikeySecret") {
            val stormpathApikeySecret = stormpathConfig.stormpathApikeySecret()

            it("should return 'api_key_secret'") {
                assertEquals("api_key_secret", stormpathApikeySecret)
            }
        }
    }

    given("a stormpath config when environment variables are not set") {
        val environmentMock = mock(javaClass<Environment>())
        `when`(environmentMock?.stormpathApplicationId())?.thenReturn("")

        val stormpathConfig = security.StormpathConfig()
        stormpathConfig.environment(environmentMock)

        on("calling stormpathApplicationRestUrl") {
            val exception = fails({ stormpathConfig.stormpathApplicationRestUrl() })

            it("should thow IllegalStateException") {
                assertEquals(javaClass<IllegalStateException>(), exception.javaClass)
            }
        }

        on("calling stormpathApikeyId") {
            val exception = fails { stormpathConfig.stormpathApikeyId() }

            it("should throw IllegalStateException") {
                assertEquals(javaClass<IllegalStateException>(), exception.javaClass)
            }
        }

        on("calling stormpathApikeySecret") {
            val exception = fails { stormpathConfig.stormpathApikeySecret() }

            it("should return 'api_key_secret'") {
                assertEquals(javaClass<IllegalStateException>(), exception.javaClass)
            }
        }
    }
}}