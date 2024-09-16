package com.toki.butler.Casino.Commands;

import com.toki.butler.Casino.Casino;
import com.toki.butler.Casino.Player;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Profile {
    public static void profileCommand(SlashCommandInteractionEvent event, User user)
    {
        Player player = Casino.getPlayer(user);
        event.reply("You have $" + player.getCash() + " cash").queue();
    }
}
