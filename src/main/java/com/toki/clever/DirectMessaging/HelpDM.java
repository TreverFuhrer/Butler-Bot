package com.toki.clever.DirectMessaging;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class HelpDM extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message currentMessage = event.getMessage();
        if(!currentMessage.getContentRaw().equalsIgnoreCase("help"))
            return;
        if(!event.isFromType(ChannelType.PRIVATE))
            return;
        User user = event.getAuthor();
        if(user.isBot())
            return;

        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(Color.GREEN);

        embed.setTitle("**Help**");
        embed.addField("Create Workout", "", true);
        embed.addField("Set Workout", "", true);
        embed.addField("Play Game", "", true);
        embed.addField("Mod Game", "", true);

        user.openPrivateChannel().queue(privateChannel ->
                privateChannel.sendMessageEmbeds(embed.build()).queue());

    }
}
