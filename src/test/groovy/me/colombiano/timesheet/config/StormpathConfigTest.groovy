package me.colombiano.timesheet.config

import me.colombiano.timesheet.environment.Environment
import spock.lang.Specification

import static me.colombiano.timesheet.environment.EnvironmentConstant.*


class StormpathConfigTest extends Specification {

    final config = new StormpathConfig()
    final environmentMock = Mock(Environment)

    final app_id = "app_id"
    final api_key_id = "api_key_id"
    final api_key_secret = "api_key_secret"

    def setup() {
        config.environment = environmentMock
    }

    def "should return application REST URL"() {
        given:
        environmentMock.stormpathApplicationId() >> app_id

        expect:
        config.stormpathApplicationRestUrl() == "https://api.stormpath.com/v1/applications/app_id"
    }

    def "should throw exception if environment base url is null"() {
        given:
        environmentMock.stormpathApplicationId() >> null

        when:
        config.stormpathApplicationRestUrl()

        then:
        final e = thrown(IllegalStateException)
        e.message == String.format(StormpathConfig.errorMessage, STORMPATH_TIMESHEET_APPLICATION_ID)
    }

    def "should return api key id"() {
        given:
        environmentMock.stormpathApikeyId() >> api_key_id

        expect:
        config.stormpathApikeyId() == "api_key_id"
    }

    def "should thow exception if environment variable api key id is null"() {
        given:
        environmentMock.stormpathApikeyId() >> null

        when:
        config.stormpathApikeyId()

        then:
        final e = thrown(IllegalStateException)
        e.message == String.format(StormpathConfig.errorMessage, STORMPATH_APIKEY_ID)
    }

    def "should return api key secret"() {
        given:
        environmentMock.stormpathApikeySecret() >> api_key_secret

        expect:
        config.stormpathApikeySecret() == "api_key_secret"
    }

    def "should throw exception if environemt api key secret is null"() {
        given:
        environmentMock.stormpathApikeySecret() >> null

        when:
        config.stormpathApikeySecret()

        then:
        final e = thrown(IllegalStateException)
        e.message == String.format(StormpathConfig.errorMessage, STORMPATH_APIKEY_SECRET)
    }
}
