package com.toki.butler;

import com.toki.butler.Casino.Commands.Brokie;
import com.toki.butler.Casino.Commands.Games.Cointoss;
import com.toki.butler.Casino.Commands.Games.Connect4;
import com.toki.butler.Casino.Commands.Games.PlayerRace;
import com.toki.butler.Casino.Commands.Profile;
import com.toki.butler.Casino.Commands.Games.Slots;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;


public class CommandManager extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        String command = event.getName();
        String userTag = user.getAsTag();


        if (command.equals("help"))
        {
            System.out.println("USER TAG: " + userTag);

            if (userTag.equals("Toki#1111"))
            {
                event.reply("Hello Master!").queue();
            }
            else
            {
                event.reply("Hi "
                        + userTag.toLowerCase().charAt(0)
                        + userTag.substring(1, userTag.indexOf("#"))
                        + "... Ummmm, who the fuck are you?").queue();
            }
        }
        else if(command.equals("slots"))
        {
            Slots.slotsCommand(event, user);
        }
        else if(command.equals("cointoss"))
        {
            Cointoss.coinCommand(event, user);
        }
        else if(command.equals("profile"))
        {
            Profile.profileCommand(event, user);
        }
        else if(command.equals("brokie"))
        {
            Brokie.brokieCommand(event, user);
        }
        else if(command.equals("connect4"))
        {
            Connect4.connect4Command(event, user);
        }
        else if(command.equals("racepvp"))
        {
            PlayerRace.playerRaceCommand(event, user);
        }
    }
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
// General
        //   /help
        commandData.add(Commands.slash("help", "Tells you all that you need to know :3"));

// Casino
        //   /profile
        commandData.add(Commands.slash("profile", "You're profile!"));
        //   /brokie
        commandData.add(Commands.slash("brokie", "If you have $0, get money here!"));
    // Games
        //   /Slots
        OptionData slotsBet = new OptionData(OptionType.NUMBER, "bet", "Amount of cash to bet", true);
        commandData.add(Commands.slash("slots", "Start a game of slots!").addOptions(slotsBet));
        //   /cointoss
        OptionData coinBet = new OptionData(OptionType.NUMBER, "bet", "Amount of cash to bet", true);
        commandData.add(Commands.slash("cointoss", "Start a game of coin toss!").addOptions(coinBet));
        //   /connect4
        OptionData c4Bet = new OptionData(OptionType.NUMBER, "bet", "Amount of cash to bet", true);
        commandData.add(Commands.slash("connect4", "Start a game of connect4!").addOptions(c4Bet));
        //   /racepvp
        OptionData racepvpBet = new OptionData(OptionType.NUMBER, "bet", "Amount of cash to bet", true);
        OptionData racepvpType = new OptionData(OptionType.STRING, "type", "Type of race you wanna play", true)
                .addChoice("Car", "car").addChoice("Bike", "bike").addChoice("Horse", "horse");
        commandData.add(Commands.slash("racepvp", "Race another player!").addOptions(racepvpBet).addOptions(racepvpType));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
