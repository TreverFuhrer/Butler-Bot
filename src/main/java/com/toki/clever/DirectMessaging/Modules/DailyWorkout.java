package com.toki.clever.DirectMessaging.Modules;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DailyWorkout extends ListenerAdapter {

    private int step;
    private String name;

    // Has various different workouts and saves the main one
    // Creates an embed message to send with the workout of that day

    enum workout1 {
        Monday()
    }

    public DailyWorkout() {

    }

    public DailyWorkout(String name) {
        this.step = 0;
        this.name = name;
    }

    public EmbedBuilder getDailyWorkout()
    {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription("You Have an OPEN Channel");

        return embed;
    }

    public void workoutCreator(String message, User user) {
        switch (step) {
            case 1:
                this.name = message;
                this.step = 2;
                this.sendMessage(user, "");
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
        }
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        User user = event.getAuthor();
        if(message.equalsIgnoreCase("create workout")) {
            this.sendMessage(user, "Creating Workout...\n" +
                    "What do you want to name your new workout?");
            this.step = 1;
        }
        if(this.step < 1)
            return;
        workoutCreator(message, user);
    }

    public void saveNewWorkout() {
        DailyWorkout newWorkout = new DailyWorkout(this.name);

        // Save object as json
    }

    private void sendMessage(User user, String message) {
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(message).queue());
    }

}
