package me.colombiano.timesheet.config

import com.google.inject.Inject
import me.colombiano.timesheet.environment.Environment
import me.colombiano.timesheet.environment.EnvironmentConstant

import static me.colombiano.timesheet.environment.EnvironmentConstant.*
import static org.apache.commons.lang3.StringUtils.isBlank
import static org.apache.commons.lang3.StringUtils.join

class StormpathConfig implements SecurityConfig {

    @Inject
    private Environment environment;

    final static errorMessage = "Environment variable %s is not defined"

    @Override
    String stormpathApplicationRestUrl() {
        final baseUrl = "https://api.stormpath.com/v1/applications/"
        final appId = environment.stormpathApplicationId()

        if (isBlank(appId)) {
            throwIllegalStateException(STORMPATH_TIMESHEET_APPLICATION_ID)
        }

        join(baseUrl, appId)
    }

    @Override
    String stormpathApikeyId() {
        final apikeyId = environment.stormpathApikeyId()

        if (isBlank(apikeyId)) {
            throwIllegalStateException(STORMPATH_APIKEY_ID)
        }

        apikeyId
    }

    @Override
    String stormpathApikeySecret() {
        final apikeySecret = environment.stormpathApikeySecret()

        if (isBlank(apikeySecret)) {
            throwIllegalStateException(STORMPATH_APIKEY_SECRET)
        }

        apikeySecret
    }

    private void throwIllegalStateException(final EnvironmentConstant constant) {
        throw new IllegalStateException(String.format(errorMessage, constant))
    }

    void setEnvironment(final Environment environment) {
        this.environment = environment
    }
}