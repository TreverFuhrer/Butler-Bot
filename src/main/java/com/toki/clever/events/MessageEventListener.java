package com.toki.clever.events;

import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;


public class MessageEventListener extends ListenerAdapter {

    int messageType = 0;
    boolean userReply = false;
    MessageChannelUnion summonChannel;
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Variables
        String message = event.getMessage().getContentDisplay().toLowerCase();

        // Sends Messages
        if(messageType == 0) {
            if (Objects.equals(event.getAuthor().getAvatarId(), "Fairy#4308") && message.contains("how may i be at your service?")) {
                messageType = 1;
                summonChannel = event.getChannel();
            }
            else if (event.getAuthor().isBot()) {
                return; // Checks if message is from bot
            }
        }
        else if(messageType == 1) {
            if(event.getChannel() == summonChannel) {
                if(message.contains("sort")) {
                    event.getChannel().sendMessage("ohhh, so you want me to sort something huh").queue();
                    messageType = 0;
                }
                else {
                    event.getChannel().sendMessage("i can service you by\n1. sorting things\n2. idk").queue();
                }
            }
        }
    }
}
