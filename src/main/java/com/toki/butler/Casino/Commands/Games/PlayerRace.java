package com.toki.butler.Casino.Commands.Games;

import com.toki.butler.Casino.Casino;
import com.toki.butler.Casino.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;

public class PlayerRace extends ListenerAdapter {
    public static void playerRaceCommand(SlashCommandInteractionEvent event, User user)
    {
        double bet;
        String type;

        // Gets user input from command
        OptionMapping optionBet = event.getOption("bet");
        OptionMapping optionType = event.getOption("type");
        if(optionBet != null && optionType != null) {
            bet = optionBet.getAsDouble();
            type = optionType.getAsString();
        } else return;

        // Check if player can bet
        Player player = Casino.getPlayer(user);
        if(player.checkBet(bet)) {
            event.reply("You don't have enough to bet that much").setEphemeral(true).queue();
            return;
        }

        // Create Message
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Player Race!");
        embed.setDescription(user.getAsMention() + " wants to race you with " + type + "s!\n" +
                "They want to bet $" + bet);
        embed.setFooter(user.getEffectiveName(), user.getAvatarUrl());

        // Send Message
        event.replyEmbeds(embed.build())
                .addActionRow(
                        Button.success("PR2ndPlayer$"+bet+":"+type, "Play!"),
                        Button.danger("PRcancel", "Cancel")
                )
                .queue();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event)
    {
        try {
            if(!(event.getMessage().getEmbeds().get(0).getTitle().equals("Player Race!"))) return;
        } catch (Exception e) { return; }

        String user1Name = event.getMessage().getEmbeds().get(0).getFooter().getText();
        User user1 = event.getGuild().getMembersByEffectiveName(user1Name, true).get(0).getUser();
        User user2 = event.getUser();

        // Check if the button ID matches
        if (event.getComponentId().contains("PR2ndPlayer"))
        {
            String id = event.getComponentId();
            double bet = Double.parseDouble(id.substring(id.indexOf('$')+1, id.indexOf(':')));
            String type = id.substring(id.indexOf(':')+1);

            // Check if same player
            //if(user1.equals(user2)) {
            //    event.reply("You cant race yourself :grimacing:").setEphemeral(true).queue();
            //    return;
            //}

            // Check if player can bet
            Player player1 = Casino.getPlayer(user1);
            Player player2 = Casino.getPlayer(user2);
            if(player2.checkBet(bet)) {
                event.editMessageEmbeds(new EmbedBuilder()
                        .setDescription("You don't have enough to bet that much")
                        .setFooter(user2.getEffectiveName(), user2.getAvatarUrl()).build()).queue();
                return;
            }

            String newMessage;
            int ran = (int) (Math.random()*2)+1; // Choose winner

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Player Race! :horse_racing::skin-tone-2:");
            embed.setFooter(user1.getEffectiveName(), user1.getAvatarUrl());

            // Set message to default game board
            String[] defaultBoard = {":green_square:",":green_square:",":green_square:",":green_square:",":green_square:",":green_square:",":green_square:",":green_square:"};
            newMessage = user1.getAsMention() + Arrays.toString(defaultBoard) + "\n"
                    + user2.getAsMention() + Arrays.toString(defaultBoard);
            embed.setDescription(newMessage);
            event.editMessageEmbeds(embed.build()).setComponents(Collections.emptyList()).queue();

            // Play game
            int track1 = 0;
            int track2 = 0;
            boolean gameOver = false;
            do {
                // Progress game
                int rand = (int) (Math.random() * 2) + 1;
                if (rand == 1) ++track1;
                else ++track2;
                System.out.println("track1: " + track1);
                System.out.println("track2: " + track2);
                // Game Board
                String[] top = defaultBoard.clone();
                String[] bot = defaultBoard.clone();
                top[track1] = ":horse_racing::skin-tone-2:";
                bot[track2] = ":horse_racing::skin-tone-2:";

                newMessage = user1.getAsMention() + Arrays.toString(top) + "\n"
                        + user2.getAsMention() + Arrays.toString(bot);

                // Update game message
                embed.setDescription(newMessage);
                event.editMessageEmbeds(embed.build()).setComponents(Collections.emptyList()).queue();

                // Check if winner
                if(track1 == 7 || track2 == 7) gameOver = true;
            } while (!gameOver);

            // Winning screen
            if(track1 == 7) { // Player 1 Wins
                player1.addCash(bet);
                player2.removeCash(bet);
                newMessage = user1.getAsMention() + " won $" + bet + "!" + "\nThey now have " + player1.getCash();
                newMessage += "\n" + user2.getAsMention() + " lost $" + bet + "..." + "\nThey now have $" + player2.getCash();
            }
            else { // Player 2 Wins
                player2.addCash(bet);
                player1.removeCash(bet);
                newMessage = user2.getAsMention() + " won $" + bet + "!" + "\nThey now have " + player2.getCash();
                newMessage += "\n" + user1.getAsMention() + " lost $" + bet + "..." + "\nThey now have $" + player1.getCash();
            }

            // Change message
            embed.setDescription(newMessage);
            event.editMessageEmbeds(embed.build()).setComponents(Collections.emptyList()).queue();
        }
        else if (event.getComponentId().equals("PRcancel")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Player Race!");
            embed.setDescription("Canceled the race");
            embed.setFooter(user1.getEffectiveName(), user1.getAvatarUrl());
            event.editMessageEmbeds(embed.build()).setComponents(Collections.emptyList()).queue();
        }
    }
}

