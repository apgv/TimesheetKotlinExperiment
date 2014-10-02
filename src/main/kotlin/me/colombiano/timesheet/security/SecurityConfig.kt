package me.colombiano.timesheet.security

trait SecurityConfig {

    fun stormpathApplicationRestUrl(): String?

    fun stormpathApikeyId(): String?

    fun stormpathApikeySecret(): String?
}