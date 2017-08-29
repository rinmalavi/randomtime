package de.atron.randomtime.root.model;

import java.time.LocalDateTime;

public class AtRandomTime {

    private LocalDateTime dateTime;

    private String randomString;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getRandomString() {
        return randomString;
    }

    public void setRandomString(String randomString) {
        this.randomString = randomString;
    }
}
