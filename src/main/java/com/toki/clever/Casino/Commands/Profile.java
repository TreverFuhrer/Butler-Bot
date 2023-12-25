package com.toki.clever.Casino.Commands;

import com.toki.clever.Casino.Casino;
import com.toki.clever.Casino.Users.Player;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Profile {
    public static void profileCommand(SlashCommandInteractionEvent event, User user)
    {
        Player player = Casino.getPlayer(user);
        event.reply("You have $" + player.getCash() + " cash").queue();
    }
}
