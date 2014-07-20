package me.colombiano.timesheet


enum Urls {
    LOGIN("/public/login.html"),
    LOGIN_SUCCESS("/public/secured/timesheet.html")

    private final String url

    private Urls(String url) {
        this.url = url
    }

    String getUrl() {
        return url
    }
}