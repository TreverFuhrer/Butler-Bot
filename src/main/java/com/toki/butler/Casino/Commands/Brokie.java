package com.toki.butler.Casino.Commands;

import com.toki.butler.Casino.Casino;
import com.toki.butler.Casino.Users.Player;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Brokie {

    public static void brokieCommand(SlashCommandInteractionEvent event, User user)
    {
        Player player = Casino.getPlayer(user);
        if(player.getCash() > 0) {
            event.reply("You aren't broke, $0 is broke...").queue();
            return;
        }
        if(player.checkUsedBrokieCmd()){
            event.reply("You already used this command today :(").queue();
            return;
        }
        player.setUsedBrokieCmdToday();
        player.addCash(10000);
        event.reply("Since your so broke, here's $10000").queue();
    }
}
