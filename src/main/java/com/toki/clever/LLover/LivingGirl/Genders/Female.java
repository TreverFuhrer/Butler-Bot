package com.toki.clever.LLover.LivingGirl.Genders;

import com.toki.clever.LLover.LivingGirl.Human;

import java.util.Random;

public class Female extends Human {

    public Female(String name) {
        super(name);
        Random random = new Random();
        this.heights = new String[]{"4'10", "4'11", "5'0", "5'1", "5'2", "5'3", "5'4", "5'5", "5'6", "5'7", "5'8", "5'9"};
        this.height = heights[random.nextInt(heights.length)];
        this.minWeight = 100.0;
        this.maxWeight = 180.0;
        this.weight = minWeight + (maxWeight - minWeight) * random.nextDouble();
    }

    // periods
}
