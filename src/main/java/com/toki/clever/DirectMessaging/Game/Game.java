package com.toki.clever.DirectMessaging.Game;

import net.dv8tion.jda.api.entities.User;

public abstract class Game {
    private final User user;

    public Game(User user) {
        this.user = user;
        run();
    }

    public User getUser() {
        return this.user;
    }

    public abstract void stopGame();

    public abstract void run();


    public void sendMessage(String message) {
        this.user.openPrivateChannel().queue(privateChannel ->
                privateChannel.sendMessage(message).queue());
    }

}
