package me.colombiano.timesheet.environment

trait Environment {

    fun stormpathApplicationId(): String?

    fun stormpathApikeyId(): String?

    fun stormpathApikeySecret(): String?

    fun pivotalWebServicesJsonString(): String?
}