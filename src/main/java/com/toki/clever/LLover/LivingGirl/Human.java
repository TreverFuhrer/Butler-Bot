package com.toki.clever.LLover.LivingGirl;

import com.toki.clever.LLover.LivingGirl.Systems.Health;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Human {

    private final String name;
    private final String birthDay;
    private Health health;

    public Human(String name) {
        this.name = name;
        this.birthDay = this.getCreationDate();
        this.health = new Health();
    }

    /*
     * Accessors
     */

    public String getName() {
        return this.name;
    }
    public String getBirthDay() {
        return this.birthDay;
    }

    /*
     * Methods
     */

    public String getCreationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return LocalDate.now().format(formatter);
    }

    /**
     * Returns a status message based on health value
     * @return status message String
     * @see com.toki.clever.LLover.LivingGirl.Systems.Health
     */
    public String getStatus() {
        int health = this.health.getHealth();
        return "I am feelings okay";
    }
}
