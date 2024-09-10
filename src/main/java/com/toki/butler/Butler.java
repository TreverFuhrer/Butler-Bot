package com.toki.butler;

import com.toki.butler.Casino.Commands.Games.Connect4;
import com.toki.butler.Casino.Commands.Games.PlayerRace;
import com.toki.butler.events.MessageEventListener;
import com.toki.butler.events.ReadyEventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Butler {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("TOKEN");
        JDABuilder builder = JDABuilder.createDefault(token);


        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setActivity(Activity.customStatus("\uD83C\uDF77" + " gang"));
        JDA jda = builder
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new ReadyEventListener(), new CommandManager(), new MessageEventListener(),
                        // Casino Games
                        new PlayerRace(), new Connect4()
                )
                .build();
    }
}
