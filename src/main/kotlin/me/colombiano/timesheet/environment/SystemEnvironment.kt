package me.colombiano.timesheet.environment

class SystemEnvironment : Environment {

    override fun stormpathApplicationId(): String? {
        return System.getenv(EnvironmentConstant.STORMPATH_TIMESHEET_APPLICATION_ID.name())
    }

    override fun stormpathApikeyId(): String? {
        return System.getenv(EnvironmentConstant.STORMPATH_APIKEY_ID.name())
    }

    override fun stormpathApikeySecret(): String? {
        return System.getenv(EnvironmentConstant.STORMPATH_APIKEY_SECRET.name())
    }

    override fun pivotalWebServicesJsonString(): String? {
        return System.getenv(EnvironmentConstant.VCAP_SERVICES.name())
    }
}