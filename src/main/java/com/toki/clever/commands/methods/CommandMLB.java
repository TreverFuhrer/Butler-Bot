package com.toki.clever.commands.methods;

import com.toki.clever.custom.MLB;
import com.toki.clever.webscraper.MLBWebScraper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.Route;
import net.dv8tion.jda.internal.entities.emoji.CustomEmojiImpl;

public class CommandMLB {

    private String[] logosMLB = {"https://cdn.discordapp.com/attachments/1157738289973973173/1157742646635282613/mlb_logo.png?ex=6519b75c&is=651865dc&hm=8dd1fc97ede1d6f6f9ba6129f26a35be36c0c5421ecca324d7030fe289eab403&",
    ""};

    public static void MLB(SlashCommandInteractionEvent event, String userTag)
    {
        // Gets user input from command - team name
        OptionMapping optionTeam = event.getOption("team");
        String teamName = optionTeam.getAsString();
        System.out.println(userTag + " used command mlb for team: " + teamName);

        // Capitalizes Team Name
        String initial = teamName.substring(0,1).toUpperCase();
        teamName = initial + teamName.substring(1);

        // Tries to change from location to team name
        // Ignored if already team name or invalid
        try {
            String name = MLB.getMLBTeamLocations().get(teamName);
            if(name != null)
                teamName = name;
        } catch (Exception ignored){}

        for(String team : MLB.getMLBTeamNames())
        {
            if(team.equalsIgnoreCase(teamName))
            {
                // Scrapes MLB website for that teams data
                MLBWebScraper.scrapeMLB(teamName);


                // Creates Team and Opponents Emojis
                String teamEmoji = MLB.getMLBTeamEmojis().get(teamName);
                //Emoji emojiTeam = new CustomEmojiImpl(teamEmoji.substring(2, teamEmoji.indexOf(":")), Long.parseLong(teamEmoji.substring(teamEmoji.lastIndexOf(":"), teamEmoji.length()-1)), false);

                String topTeam = MLBWebScraper.getTopTeam();
                String bottomTeam = MLBWebScraper.getBottomTeam();
                String topTeamEmoji = MLB.getMLBTeamEmojis().get(topTeam);
                String bottomTeamEmoji = MLB.getMLBTeamEmojis().get(bottomTeam);

                // Create Embed Message
                EmbedBuilder embed = new EmbedBuilder();
                String gameState = MLBWebScraper.getCurrentGameState();

                // Game State is Starting Time of Game Today
                if(gameState.substring(gameState.length()-2).equals("ET"))
                {
                    embed.setTitle("**Today** at **" + gameState.substring(0, gameState.indexOf("E")-1) + "** ET" + gameState.substring(gameState.indexOf("T")+1));

                    embed.setDescription(topTeamEmoji + " **" + topTeam + "** " + MLBWebScraper.getTopTeamWinRatio() + "\n"
                            + bottomTeamEmoji + " **" + bottomTeam + "** " + MLBWebScraper.getBottomTeamWinRatio());

                    //embed.setThumbnail(Emoji);

                    embed.addField(".1 2 3 4 5 6 7 8 9  - R H E", MLBWebScraper.getScoreState(), true);
                }
                else {
                    embed.setTitle(gameState);

                    embed.setDescription(topTeamEmoji + " **" + topTeam + "** " + MLBWebScraper.getTopTeamWinRatio() + "\n"
                            + bottomTeamEmoji + " **" + bottomTeam + "** " + MLBWebScraper.getBottomTeamWinRatio());

                    //embed.setThumbnail("https://cdn.discordapp.com/attachments/1157738289973973173/1157742646635282613/mlb_logo.png?ex=6519b75c&is=651865dc&hm=8dd1fc97ede1d6f6f9ba6129f26a35be36c0c5421ecca324d7030fe289eab403&");

                    embed.addField(".1 2 3 4 5 6 7 8 9  - R H E", MLBWebScraper.getScoreState(), true);
                }

                // Sets Embed Color To Teams Color
                embed.setColor(MLB.getMLBTeamColors().get(teamName));

                //embed.setImage("https://midfield.mlbstatic.com/v1/people/642216/silo/60?zoom=1.2.png");

                // Send the embed message
                event.replyEmbeds(embed.build()).queue();
                return;
            }
        }
        event.reply(teamName + " is not a valid team :((").setEphemeral(true).queue();
    }
}
