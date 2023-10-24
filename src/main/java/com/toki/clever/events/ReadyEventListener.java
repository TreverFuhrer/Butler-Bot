package com.toki.clever.events;

import com.toki.clever.Day.DailyDM;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class ReadyEventListener implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent event)
    {
        if (event instanceof ReadyEvent) {
            System.out.println("Im here to help Master!");

            JDA jda = event.getJDA();
            DailyDM.Daily(jda);


            //MLBWebScraper.scrapeMLB("Braves");
            //System.out.println(MLBWebScraper.getTopTeam());
            //System.out.println(MLBWebScraper.getBottomTeam());
        }
    }
}
