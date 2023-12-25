package com.toki.clever.LLover.LivingGirl.Genders;

import com.toki.clever.LLover.LivingGirl.Human;

import java.util.Random;

public class Male extends Human {

    public Male(String name) {
        super(name);
        Random random = new Random();
        this.heights = new String[]{"5'6", "5'7", "5'8", "5'9", "5'10", "5'11", "6'0", "6'1", "6'2", "6'3", "6'4"};
        this.height = heights[random.nextInt(heights.length)];
        this.minWeight = 140.0;
        this.maxWeight = 220.0;
        this.weight = minWeight + (maxWeight - minWeight) * random.nextDouble();
    }
}
