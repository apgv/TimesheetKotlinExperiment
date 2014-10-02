package me.colombiano.timesheet.config

import me.colombiano.timesheet.environment.Environment
import me.colombiano.timesheet.environment.SystemEnvironment
import org.apache.commons.lang3.StringUtils.*
import me.colombiano.timesheet.environment.EnvironmentConstant
import me.colombiano.timesheet.environment.EnvironmentConstant.*


class StormpathConfig() : SecurityConfig {

    private val errorMessage = "Environment variable %s is not defined"
    private var environment: Environment? = SystemEnvironment()

    fun environment(environment: Environment?) {
        this.environment = environment
    }

    override fun stormpathApplicationRestUrl(): String? {
        val baseUrl = "https://api.stormpath.com/v1/applications/"
        val applicationId = environment?.stormpathApplicationId()

        if (isBlank(applicationId)) {
            throwIllegalStateException(STORMPATH_TIMESHEET_APPLICATION_ID)
        }

        return join(baseUrl, applicationId)
    }

    override fun stormpathApikeyId(): String? {
        val apikeyId = environment?.stormpathApikeyId()

        if (isBlank(apikeyId)) {
            throwIllegalStateException(STORMPATH_APIKEY_ID)
        }

        return apikeyId
    }

    override fun stormpathApikeySecret(): String? {
        val apikeySecret = environment?.stormpathApikeySecret()

        if (isBlank(apikeySecret)) {
            throwIllegalStateException(STORMPATH_APIKEY_SECRET)
        }

        return apikeySecret
    }

    private fun throwIllegalStateException(environmentConstant: EnvironmentConstant) {
        throw IllegalStateException(java.lang.String.format(errorMessage, environmentConstant))
    }
}