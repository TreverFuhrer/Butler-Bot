package com.toki.butler.Casino;

import com.toki.butler.Casino.Users.Player;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Casino {
    private static final HashMap<String, Player> players= new HashMap<>();

    public static Player getPlayer(User user) {
        String ID = user.getId();
        while(true) {
            Player player = Casino.players.get(ID);
            if(player != null)
                return player;
            if(!Casino.loadPlayer(ID)) {
                Player newPlayer = new Player(ID, 1000.0);
                Casino.loadPlayer(ID);
                return newPlayer;
            }
        }
    }

    public static boolean loadPlayer(String ID) {
        Path path = Paths.get("data/casino.json");
        try {
            if (Files.notExists(path) || Files.size(path) == 0)
                return false;
            String content = Files.readString(path);
            JSONObject obj = new JSONObject(content);

            if (!obj.has(ID))
                return false;

            JSONObject playerJson = obj.getJSONObject(ID);
            double cash = playerJson.getDouble("cash");

            Player player = new Player(ID, cash);
            players.put(ID, player);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
