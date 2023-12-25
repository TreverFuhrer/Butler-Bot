package com.toki.clever.Casino.Commands.Games;

import com.toki.clever.Casino.Casino;
import com.toki.clever.Casino.Users.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

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
        if(bet > player.getCash()) {
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
            if(user1.equals(user2)) {
                event.reply("You cant race yourself :grimacing:").setEphemeral(true).queue();
                return;
            }

            // Check if player can bet
            Player player1 = Casino.getPlayer(user1);
            Player player2 = Casino.getPlayer(user2);
            if(bet > player2.getCash()) {
                event.editMessageEmbeds(new EmbedBuilder()
                        .setDescription("You don't have enough to bet that much")
                        .setFooter(user2.getEffectiveName(), user2.getAvatarUrl()).build()).queue();
                return;
            }
            String newMessage;
            int ran = (int) (Math.random()*2)+1;
            if(ran == 1) {
                player1.addCash(bet);
                player2.removeCash(bet);
                newMessage = user1.getAsMention() + " won $" + bet + "!" + "\nThey now have " + player1.getCash();
                newMessage += "\n" + user2.getAsMention() + " lost $" + bet + "..." + "\nThey now have $" + player2.getCash();
            }
            else {
                player2.addCash(bet);
                player1.removeCash(bet);
                newMessage = user2.getAsMention() + " won $" + bet + "!" + "\nThey now have " + player2.getCash();
                newMessage += "\n" + user1.getAsMention() + " lost $" + bet + "..." + "\nThey now have $" + player1.getCash();
            }
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Player Race!");
            embed.setDescription(newMessage);
            embed.setFooter(user1.getEffectiveName(), user1.getAvatarUrl());
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

