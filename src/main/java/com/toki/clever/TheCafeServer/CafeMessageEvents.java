package com.toki.clever.TheCafeServer;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CafeMessageEvents extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        if (user.isBot()) return;

        String message = event.getMessage().getContentRaw().toLowerCase();
        if (message.matches(".*(hi llover|hello llover|howdy llover|hey llover|ello llover|helo llover).*")) {
            String[] greetings = {"hello", "hi", "hey", "howdy"};
            int index = (int)(Math.random()*3)+1;
            event.getChannel().sendMessage(greetings[index] + " " + user.getEffectiveName()).queue();
        }

    }
}
