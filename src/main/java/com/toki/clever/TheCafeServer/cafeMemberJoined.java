package com.toki.clever.TheCafeServer;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class cafeMemberJoined extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event)
    {
        Guild guild = event.getGuild();
        if(!guild.getId().equals("1175533052961759362")) return;

        User user = event.getUser();
        TextChannel channel = guild.getTextChannelById("1175605228863766558");
        if (channel != null)  // user.getAsMention() + ", To edit role, say role"
            channel.sendMessage("Hey " + user.getAsMention() +
                    ", everyone in this server gets a custom role!" + "\n" +
                    "Just say \"role\" :gift_heart:").queue();
        else
            System.out.println("cafeMemberJoined: channel is null");
    }
}
