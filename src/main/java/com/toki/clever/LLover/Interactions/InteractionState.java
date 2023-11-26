package com.toki.clever.LLover.Interactions;

import net.dv8tion.jda.api.entities.User;

public class InteractionState {

    private String currentTopic;
    private User user;


    public InteractionState(User user) {
        this.currentTopic = "greeting";
        this.user = user;
    }

    public String getCurrentTopic() {
        return this.currentTopic;
    }

    public void setCurrentTopic(String currentTopic) {
        this.currentTopic = currentTopic;
    }
}
