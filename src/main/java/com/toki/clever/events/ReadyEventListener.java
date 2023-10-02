package com.toki.clever.events;

import com.toki.clever.webscraper.MLBWebScraper;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class ReadyEventListener implements EventListener {

    @Override
    public void onEvent(GenericEvent event)
    {
        if (event instanceof ReadyEvent) {
            System.out.println("Im here to help Master!");

            //MLBWebScraper.scrapeMLB("Braves");
            //System.out.println(MLBWebScraper.getTopTeam());
            //System.out.println(MLBWebScraper.getBottomTeam());
        }
    }
}
