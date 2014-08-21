package me.colombiano.timesheet.environment


interface Environment {

    String stormpathApplicationId()

    String stormpathApikeyId()

    String stormpathApikeySecret()

    String cloudFoundryServices()

}