package com.toki.clever;

import com.toki.clever.commands.CommandManager;
import com.toki.clever.events.MessageEventListener;
import com.toki.clever.events.ReadyEventListener;
import com.toki.clever.webscraper.MLBWebScraper;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class Clever {

    public static void main(String[] args) throws IOException {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("TOKEN");
        JDABuilder builder = JDABuilder.createDefault(token);


        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setActivity(Activity.watching("Gay Porn"));
        JDA jda = builder
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new ReadyEventListener(), new CommandManager(), new MessageEventListener())
                .build();

    }
}
