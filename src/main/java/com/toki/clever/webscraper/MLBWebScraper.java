package com.toki.clever.webscraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MLBWebScraper {

    private static String topTeam;
    private static String bottomTeam;
    private static String topTeamWinRatio;
    private static String bottomTeamWinRatio;
    private static String currentGameState;
    private static String scoreState;

    public static String getTopTeam() {
        return MLBWebScraper.topTeam;
    }
    public static String getBottomTeam() {
        return MLBWebScraper.bottomTeam;
    }

    public static String getTopTeamWinRatio() {
        return MLBWebScraper.topTeamWinRatio;
    }
    public static String getBottomTeamWinRatio() {
        return MLBWebScraper.bottomTeamWinRatio;
    }

    public static String getCurrentGameState() {
        return MLBWebScraper.currentGameState;
    }
    public static String getScoreState() {
        return MLBWebScraper.scoreState;
    }

    public static void scrapeMLB(String teamName) {

        // Fixes D-Backs names if other spelling used
        if(teamName.equalsIgnoreCase("DiamondBacks") || teamName.equalsIgnoreCase("Dbacks")) {
            teamName = "D-backs";
        }

        // Gets Today's Date, Example: 2023-10-01
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String formattedDate = dtf.format(localDate);

        try {
            Document doc = Jsoup.connect("https://www.mlb.com/scores/" + formattedDate).get();
            Elements divs = doc.select("div.TeamWrappersstyle__DesktopTeamWrapper-sc-uqs6qh-0");

            for (Element div : divs) {
                if (div.text().equalsIgnoreCase(teamName))
                {
                    // Gets Win Ratio of Users Team
                    Element winRatioDiv = div.parents().get(1).child(1);

                    // Assume bottom div is team and top is op
                    Element opNameDiv = winRatioDiv.parents().get(4).child(1).child(0).child(0).child(1).child(0).child(0);
                    // Check if top is same as bottom if so then op is actually bottom div
                    if(opNameDiv.text().equals(teamName)) {
                        opNameDiv = winRatioDiv.parents().get(4).child(2).child(0).child(0).child(1).child(0).child(0);
                        MLBWebScraper.topTeam = teamName;
                        MLBWebScraper.bottomTeam = opNameDiv.text();
                        MLBWebScraper.topTeamWinRatio = winRatioDiv.text();
                    }
                    else {
                        MLBWebScraper.topTeam = opNameDiv.text();
                        MLBWebScraper.bottomTeam = teamName;
                        MLBWebScraper.bottomTeamWinRatio = winRatioDiv.text();
                        MLBWebScraper.topTeamWinRatio = "Opponent Team";
                    }
                    // Gets Win Ratio of Opponent Team
                    Element winRatioOpponentDiv = opNameDiv.parents().get(1).child(1);

                    // Sets team ratios to correct position
                    if(MLBWebScraper.topTeamWinRatio.equals(winRatioDiv.text()))
                        MLBWebScraper.bottomTeamWinRatio = winRatioOpponentDiv.text();
                    else
                        MLBWebScraper.topTeamWinRatio = winRatioOpponentDiv.text();


                    // If game isn't active then there will be an Exception
                    try {
                        Element gameState = winRatioDiv.parents().get(8).child(0).child(0).child(0).child(0);
                        MLBWebScraper.currentGameState = gameState.text();

                        // Runs Score State Method
                        MLBWebScraper.scoreState = scoreState(winRatioDiv);
                    }
                    catch (Exception e)
                    {
                        Element gameStartTime = winRatioDiv.parents().get(8).child(0).child(0).child(0);
                        MLBWebScraper.currentGameState = gameStartTime.text();
                        MLBWebScraper.scoreState = "0 0 0 0 0 0 0 0 0 - 0 0 0" + "\n" + "0 0 0 0 0 0 0 0 0 - 0 0 0";
                        return;
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("Failed");
            e.printStackTrace();
        }
    }

    private static String scoreState(Element originDiv) {
        String scoreState = "";

        Element grandParentDiv = originDiv.parents().get(6).child(0).child(0);
        Element tableParentDiv = grandParentDiv.child(0).child(0).child(0).child(1);
        Element scoreParentDiv = grandParentDiv.child(1).child(0).child(0).child(1);

        Element topRowDiv = tableParentDiv.child(0);
        Element bottomRowDiv = tableParentDiv.child(1);

        Element topScoreDiv = scoreParentDiv.child(0);
        Element bottomScoreDiv = scoreParentDiv.child(1);

        // Adds top bar "0 0 0 0 0 0 0 0 0" + " - "
        for(int k = 1; k <= 9; k++) {
            String text = topRowDiv.child(k).text();
            if(text.equals(""))
                scoreState += ".. ";
            else if(text.equals("1"))
                scoreState += ".1 ";
            else
                scoreState += text + " ";
        }
        scoreState += " - ";
        // Adds top bar RHE scores "0 0 0"
        for(int k = 0; k < 3; k++) {
            String text = topScoreDiv.child(k).text();
            scoreState += text + " ";
        }
        scoreState += "\n";
        // Adds bottom bar "0 0 0 0 0 0 0 0 0" + " - "
        for(int k = 1; k <= 9; k++) {
            String text = bottomRowDiv.child(k).text();
            if(text.equals(""))
                scoreState += ".. ";
            else if(text.equals("1"))
                scoreState += ".1 ";
            else
                scoreState += text + " ";
        }
        scoreState += " - ";
        // Adds bottom bar RHE scores "0 0 0"
        for(int k = 0; k < 3; k++) {
            String text = bottomScoreDiv.child(k).text();
            scoreState += text + " ";
        }
        return scoreState;
    }
}
