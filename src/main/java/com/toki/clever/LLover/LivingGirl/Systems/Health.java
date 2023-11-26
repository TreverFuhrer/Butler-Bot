package com.toki.clever.LLover.LivingGirl.Systems;

public class Health {

    private int Health;

    public Health() {
        this.Health = 100;
    }


    /*
     * Accessors
     */

    public int getHealth() {
        return this.Health;
    }


    /*
     * Methods
     */

    /**
     * Decreases Health
     * @param dec Decrease Amount
     */
    public void decreaseHealth(int dec) {
        this.Health -= dec;
    }

    /**
     * Increases Health
     * @param inc Increase Amount
     */
    public void increaseHealth(int inc) {
        this.Health += inc;
    }
}
