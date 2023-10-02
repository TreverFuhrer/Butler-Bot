package com.toki.clever.commands;

import com.toki.clever.commands.methods.CommandMLB;
import net.dv8tion.jda.api.entities.emoji.Emoji;
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

        String command = event.getName();
        String userTag = event.getUser().getAsTag();

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
        else if(command.equals("mlb"))
        {
            CommandMLB.MLB(event, userTag);
        }
    }
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        //   /help
        commandData.add(Commands.slash("help", "Tells you all that you need to know :3"));

        //   /MLB
        OptionData teamName = new OptionData(OptionType.STRING, "team", "Name of team. Ex. Braves Ex. brewers", true);
        commandData.add(Commands.slash("mlb", "Get Live MLB Data On Any Team!").addOptions(teamName));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
