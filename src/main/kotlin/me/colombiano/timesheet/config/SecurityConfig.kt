package me.colombiano.timesheet.config


trait SecurityConfig {

    fun stormpathApplicationRestUrl(): String?

    fun stormpathApikeyId(): String?

    fun stormpathApikeySecret(): String?
}