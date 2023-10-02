package com.toki.clever.custom;

import java.awt.*;
import java.util.HashMap;

public class MLB {

    private static final String[] MLBTeamNames = {
            "Orioles",
            "Red Sox",
            "White Sox",
            "Guardians",
            "Tigers",
            "Astros",
            "Royals",
            "Angels",
            "Twins",
            "Yankees",
            "Athletics",
            "Mariners",
            "Rays",
            "Rangers",
            "Blue Jays",
            "Diamondbacks",
            "Dbacks",
            "D-backs",
            "Braves",
            "Cubs",
            "Reds",
            "Rockies",
            "Dodgers",
            "Marlins",
            "Brewers",
            "Mets",
            "Phillies",
            "Pirates",
            "Padres",
            "Giants",
            "Cardinals",
            "Nationals"
    };


    private static final HashMap<String, String> MLBTeamLocations = MLB.createLocations();
    private static final HashMap<String, Color> MLBTeamColors = MLB.createColors();
    private static final HashMap<String, String> MLBTeamEmojis = MLB.createEmojis();


    /* Variable Getters
     *
     *  MLBTeamColors, MLBTeamEmojis
     */
    public static String[] getMLBTeamNames() {
        return MLB.MLBTeamNames;
    }
    public static HashMap<String, String> getMLBTeamLocations() {
        return MLB.MLBTeamLocations;
    }
    public static HashMap<String, Color> getMLBTeamColors() {
        return MLB.MLBTeamColors;
    }
    public static HashMap<String, String> getMLBTeamEmojis() {
        return MLB.MLBTeamEmojis;
    }

    /**
     * Create MLB Team Hashmap with team colors
     * @return HashMap<String, Color>
     */
    private static HashMap<String, Color> createColors() {
        HashMap<String, Color> namedColors = new HashMap<>();
        // American League
        namedColors.put("Orioles", new Color(250, 70, 22));
        namedColors.put("Red Sox", new Color(0, 43, 92));
        namedColors.put("White Sox", new Color(196, 206, 212));
        namedColors.put("Guardians", new Color(227, 25, 55));
        namedColors.put("Tigers", new Color(0, 32, 91));
        namedColors.put("Astros", new Color(0, 45, 98));
        namedColors.put("Royals", new Color(26, 71, 132));
        namedColors.put("Angels", new Color(186, 12, 47));
        namedColors.put("Twins", new Color(0, 43, 92));
        namedColors.put("Yankees", new Color(12, 35, 64));
        namedColors.put("Athletics", new Color(0, 56, 168));
        namedColors.put("Mariners", new Color(12, 35, 64));
        namedColors.put("Bay Rays", new Color(8, 45, 92));
        namedColors.put("Rangers", new Color(0, 28, 71));
        namedColors.put("Blue Jays", new Color(0, 45, 98));

        namedColors.put("Baltimore", new Color(250, 70, 22));
        namedColors.put("Boston", new Color(0, 43, 92));
        namedColors.put("Chicago2", new Color(196, 206, 212));
        namedColors.put("Cleveland", new Color(227, 25, 55));
        namedColors.put("Detroit", new Color(0, 32, 91));
        namedColors.put("Houston ", new Color(0, 45, 98));
        namedColors.put("Kansas", new Color(26, 71, 132));
        namedColors.put("Los Angeles2", new Color(186, 12, 47));
        namedColors.put("Minnesota", new Color(0, 43, 92));
        namedColors.put("New York", new Color(12, 35, 64));
        namedColors.put("Oakland", new Color(0, 56, 168));
        namedColors.put("Seattle", new Color(12, 35, 64));
        namedColors.put("Tampa", new Color(8, 45, 92));
        namedColors.put("Texas", new Color(0, 28, 71));
        namedColors.put("Toronto", new Color(0, 45, 98));

        // National League
        namedColors.put("Diamondbacks", new Color(167, 25, 48));
        namedColors.put("Dbacks", new Color(167, 25, 48));
        namedColors.put("D-backs", new Color(167, 25, 48));
        namedColors.put("Braves", new Color(206, 17, 65));
        namedColors.put("Cubs", new Color(14, 51, 134));
        namedColors.put("Reds", new Color(213, 0, 50));
        namedColors.put("Rockies", new Color(51, 0, 114));
        namedColors.put("Dodgers", new Color(0, 43, 92));
        namedColors.put("Marlins", new Color(0, 163, 224));
        namedColors.put("Brewers", new Color(0, 45, 98));
        namedColors.put("Mets", new Color(0, 43, 92));
        namedColors.put("Phillies", new Color(252, 76, 76));
        namedColors.put("Pirates", new Color(253, 184, 39));
        namedColors.put("Padres", new Color(4, 30, 66));
        namedColors.put("Giants", new Color(254, 90, 29));
        namedColors.put("Cardinals", new Color(196, 30, 58));
        namedColors.put("Nationals", new Color(214, 31, 63));

        namedColors.put("Arizona", new Color(167, 25, 48));
        namedColors.put("Atlanta", new Color(206, 17, 65));
        namedColors.put("Chicago", new Color(14, 51, 134));
        namedColors.put("Cincinnati", new Color(213, 0, 50));
        namedColors.put("Colorado", new Color(51, 0, 114));
        namedColors.put("Los Angeles", new Color(0, 43, 92));
        namedColors.put("Miami", new Color(0, 163, 224));
        namedColors.put("Milwaukee", new Color(0, 45, 98));
        namedColors.put("New York2", new Color(0, 43, 92));
        namedColors.put("Philadelphia", new Color(252, 76, 76));
        namedColors.put("Pittsburgh", new Color(253, 184, 39));
        namedColors.put("San Diego", new Color(4, 30, 66));
        namedColors.put("San Francisco", new Color(254, 90, 29));
        namedColors.put("St. Louis", new Color(196, 30, 58));
        namedColors.put("Washington", new Color(214, 31, 63));

        return namedColors;
    }

    /**
     * Create MLB Team Hashmap with team Emojis
     * @return HashMap<String, String>
     */
    private static HashMap<String, String> createEmojis() {
        HashMap<String, String> teamEmojis = new HashMap<>();
        // American League
        teamEmojis.put("Baltimore", "<:Orioles:1157416514257956934>");
        teamEmojis.put("Orioles", "<:Orioles:1157416514257956934>");
        teamEmojis.put("Boston", "<:RedSox:1157416850909573192>");
        teamEmojis.put("Red Sox", "<:RedSox:1157416850909573192>");
        teamEmojis.put("Chicago2", "<:WhiteSox:1157416884963131562>");
        teamEmojis.put("White Sox", "<:WhiteSox:1157416884963131562>");
        teamEmojis.put("Cleveland", "<:Guardians:1157416503596032020>");
        teamEmojis.put("Guardians", "<:Guardians:1157416503596032020>");
        teamEmojis.put("Detroit", "<:Tigers:1157416883746779137>");
        teamEmojis.put("Tigers", "<:Tigers:1157416883746779137>");
        teamEmojis.put("Houston", "<:Astros:1157416504334221353>");
        teamEmojis.put("Astros", "<:Astros:1157416504334221353>");
        teamEmojis.put("Kansas City", "<:Royals:1157416517625987162>");
        teamEmojis.put("Royals", "<:Royals:1157416517625987162>");
        teamEmojis.put("Los Angeles2", "<:Angels:1157416500123148378>");
        teamEmojis.put("Angels", "<:Angels:1157416500123148378>");
        teamEmojis.put("Minnesota", "<:Twins:1157416521937723533>");
        teamEmojis.put("Twins", "<:Twins:1157416521937723533>");
        teamEmojis.put("New York", "<:Yankees:1157416525582577824>");
        teamEmojis.put("Yankees", "<:Yankees:1157416525582577824>");
        teamEmojis.put("Oakland", "<:Athletics:1157416509044445185>");
        teamEmojis.put("Athletics", "<:Athletics:1157416509044445185>");
        teamEmojis.put("Seattle", "<:Mariners:1157416815547392100>");
        teamEmojis.put("Mariners", "<:Mariners:1157416815547392100>");
        teamEmojis.put("Tampa Bay", "<:Rays:1157416850100072529>");
        teamEmojis.put("Rays", "<:Rays:1157416850100072529>");
        teamEmojis.put("Texas", "<:Rangers:1157416848866934945>");
        teamEmojis.put("Rangers", "<:Rangers:1157416848866934945>");
        teamEmojis.put("Toronto", "<:BlueJays:1157416510680215552>");
        teamEmojis.put("Blue Jays", "<:BlueJays:1157416510680215552>");

        // National League
        teamEmojis.put("Arizona", "<:Dbacks:1157416813152436325>");
        teamEmojis.put("Diamondbacks", "<:Dbacks:1157416813152436325>");
        teamEmojis.put("Dbacks", "<:Dbacks:1157416813152436325>");
        teamEmojis.put("D-backs", "<:Dbacks:1157416813152436325>");
        teamEmojis.put("Atlanta", "<:Braves:1157371139983675415>");
        teamEmojis.put("Braves", "<:Braves:1157371139983675415>");
        teamEmojis.put("Chicago", "<:Cubs:1157371168949534862>");
        teamEmojis.put("Cubs", "<:Cubs:1157371168949534862>");
        teamEmojis.put("Cincinnati", "<:Reds:1157416501146570752>");
        teamEmojis.put("Reds", "<:Reds:1157416501146570752>");
        teamEmojis.put("Colorado", "<:Rockies:1157416882287157318>");
        teamEmojis.put("Rockies", "<:Rockies:1157416882287157318>");
        teamEmojis.put("Los Angeles", "<:Dodgers:1157416814071005274>");
        teamEmojis.put("Dodgers", "<:Dodgers:1157416814071005274>");
        teamEmojis.put("Miami", "<:Marlins:1157416816457556059>");
        teamEmojis.put("Marlins", "<:Marlins:1157416816457556059>");
        teamEmojis.put("Milwaukee", "<:Brewers:1157416758538408026>");
        teamEmojis.put("Brewers", "<:Brewers:1157416758538408026>");
        teamEmojis.put("New York2", "<:Mets:1157416817371914371>");
        teamEmojis.put("Mets", "<:Mets:1157416817371914371>");
        teamEmojis.put("Philadelphia", "<:Phillies:1157416818189795359>");
        teamEmojis.put("Phillies", "<:Phillies:1157416818189795359>");
        teamEmojis.put("Pittsburgh", "<:Pirates:1157416848086802472>");
        teamEmojis.put("Pirates", "<:Pirates:1157416848086802472>");
        teamEmojis.put("San Diego", "<:Padres:1157416506775326760>");
        teamEmojis.put("Padres", "<:Padres:1157416506775326760>");
        teamEmojis.put("San Francisco", "<:Giants:1157416507886809118>");
        teamEmojis.put("Giants", "<:Giants:1157416507886809118>");
        teamEmojis.put("St. Louis", "<:Cardinals:1157416768898351194>");
        teamEmojis.put("Cardinals", "<:Cardinals:1157416768898351194>");
        teamEmojis.put("Washington", "<:Nationals:1157416505810628608>");
        teamEmojis.put("Nationals", "<:Nationals:1157416505810628608>");

        return teamEmojis;
    }

    /**
     * Create MLB Team Hashmap with team Locations to Names
     * @return HashMap<String, String>
     */
    private static HashMap<String, String> createLocations() {
        HashMap<String, String> teamLocations = new HashMap<>();

        teamLocations.put("Baltimore", "Orioles");
        teamLocations.put("Boston", "Red Sox");
        teamLocations.put("Chicago2", "White Sox");
        teamLocations.put("Cleveland", "Guardians");
        teamLocations.put("Detroit", "Tigers");
        teamLocations.put("Houston", "Astros");
        teamLocations.put("Kansas City", "Royals");
        teamLocations.put("Los Angeles2", "Angels");
        teamLocations.put("Minnesota", "Twins");
        teamLocations.put("New York", "Yankees");
        teamLocations.put("Oakland", "Athletics");
        teamLocations.put("Seattle", "Mariners");
        teamLocations.put("Tampa Bay", "Rays");
        teamLocations.put("Texas", "Rangers");
        teamLocations.put("Toronto", "Blue Jays");
        teamLocations.put("Arizona", "Diamondbacks");
        teamLocations.put("Atlanta", "Braves");
        teamLocations.put("Chicago", "Cubs");
        teamLocations.put("Cincinnati", "Reds");
        teamLocations.put("Colorado", "Rockies");
        teamLocations.put("Los Angeles", "Dodgers");
        teamLocations.put("Miami", "Marlins");
        teamLocations.put("Milwaukee", "Brewers");
        teamLocations.put("New York2", "Mets");
        teamLocations.put("Philadelphia", "Phillies");
        teamLocations.put("Pittsburgh", "Pirates");
        teamLocations.put("San Diego", "Padres");
        teamLocations.put("San Francisco", "Giants");
        teamLocations.put("St. Louis", "Cardinals");
        teamLocations.put("Washington", "Nationals");

        return teamLocations;
    }
}
