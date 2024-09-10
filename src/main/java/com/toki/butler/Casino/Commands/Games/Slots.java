package com.toki.butler.Casino.Commands.Games;

import com.toki.butler.Casino.Casino;
import com.toki.butler.Casino.Users.Player;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Slots {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public static void slotsCommand(SlashCommandInteractionEvent event, User user) {
        // Gets user input from command - team name
        OptionMapping optionBet = event.getOption("bet");
        double bet;
        if(optionBet != null)
            bet = optionBet.getAsDouble();
        else return;

        Player player = Casino.getPlayer(user);
        if(bet > player.getCash()) {
            event.reply("You don't have enough to bet that much").setEphemeral(true).queue();
            return;
        }

        event.reply("flipping a coin...").queue(sentMessage -> {
            // Schedule a task to be executed after a delay
            scheduler.schedule(() -> {
                String newMessage;
                int ran = (int) (Math.random()*2)+1;
                if(ran == 1) {
                    player.addCash(bet);
                    newMessage = "You Won " + (int) bet + " !" + "\nYou now have " + player.getCash();
                }
                else {
                    player.removeCash(bet);
                    newMessage = "You lost " + (int) bet + " :(" + "\nYou now have " + player.getCash();
                }
                sentMessage.editOriginal("\n" + newMessage).queue();
            }, 2, TimeUnit.SECONDS); // Delay of 2 seconds
        });
    }
}
