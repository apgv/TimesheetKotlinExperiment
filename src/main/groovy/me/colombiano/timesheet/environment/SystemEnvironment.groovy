package me.colombiano.timesheet.environment

import static EnvironmentConstant.STORMPATH_APIKEY_ID
import static EnvironmentConstant.STORMPATH_APIKEY_SECRET
import static EnvironmentConstant.STORMPATH_TIMESHEET_APPLICATION_ID

class SystemEnvironment implements Environment {
    @Override
    String stormpathApplicationId() {
        System.getenv(STORMPATH_TIMESHEET_APPLICATION_ID.name())
    }

    @Override
    String stormpathApikeyId() {
        System.getenv(STORMPATH_APIKEY_ID.name())
    }

    @Override
    String stormpathApikeySecret() {
        System.getenv(STORMPATH_APIKEY_SECRET.name())
    }

    @Override
    String cloudFoundryServices() {
        System.getenv(EnvironmentConstant.VCAP_SERVICES.name())
    }
}
