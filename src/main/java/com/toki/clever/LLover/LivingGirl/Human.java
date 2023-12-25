package com.toki.clever.LLover.LivingGirl;

import com.toki.clever.LLover.LivingGirl.Stats.Health;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public abstract class Human {

    // Default Variables
    protected final String name;
    protected final String birthDay;
    protected String[] hairColors = {"Black", "Brown", "Blonde", "Red", "Auburn", "White", "Grey"};
    protected String[] eyeColors = {"Brown", "Blue", "Green", "Hazel", "Amber", "Grey"};
    protected String[] skinColors = {"Fair", "Light", "Medium", "Tan", "Dark", "Deep"};
    protected String[] heights;  // Defined in Male or Female Classes
    protected double minWeight;
    protected double maxWeight;
    protected String hairColor;
    protected String eyeColor;
    protected String skinColor;
    protected String height;
    protected double weight;


    // Stats
    protected Health health;

    public Human(String name) {
        // Variable
        this.name = name;
        this.birthDay = this.getCreationDate();
        Random random = new Random();
        this.hairColor = hairColors[random.nextInt(hairColors.length)];
        this.eyeColor = eyeColors[random.nextInt(eyeColors.length)];
        this.skinColor = skinColors[random.nextInt(skinColors.length)];
        // Stats
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
    public String getHairColor() {
        return this.hairColor;
    }
    public String getEyeColor() {
        return this.eyeColor;
    }
    public String getSkinColor() {
        return this.skinColor;
    }
    public String getHeight() {
        return this.height;
    }
    public double getWeight() {
        return this.weight;
    }

    /*
     * Methods
     */

    private String getCreationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return LocalDate.now().format(formatter);
    }

    /**
     * Returns a status message based on health value
     * @return status message String
     * @see com.toki.clever.LLover.LivingGirl.Stats.Health
     */
    public String getStatus() {
        int health = this.health.getHealth();
        return "I am feelings okay";
    }
}
