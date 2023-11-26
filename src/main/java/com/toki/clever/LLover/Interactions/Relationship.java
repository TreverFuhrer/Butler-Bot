package com.toki.clever.LLover.Interactions;

import net.dv8tion.jda.api.entities.User;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Relationship {

    private final User user;
    private final Map<String, String> userData;
    private String relationshipStatus;

    public Relationship(User user) {
        this.user = user;
        this.userData = this.loadUserData();
    }

    /*
     * Getters
     */

    public int getRelationshipValue() {
        this.userData.putIfAbsent("relationshipValue", "0");
        this.saveUserData();
        return Integer.parseInt(this.userData.get("relationshipValue"));
    }
    public int getTimesChatted() {
        this.userData.putIfAbsent("timesChatted", "0");
        this.saveUserData();
        return Integer.parseInt(this.userData.get("timesChatted"));
    }
    public int getDaysChatted() {
        this.userData.putIfAbsent("daysChatted", "0");
        this.saveUserData();
        return Integer.parseInt(this.userData.get("daysChatted"));
    }
    public String getLastChatDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        this.userData.putIfAbsent("lastChatDay", LocalDate.now().format(formatter));
        this.saveUserData();
        return this.userData.get("lastChatDay");
    }
    public String getRelationshipStatus() {
        return this.relationshipStatus;
    }

    /*
     * Special Getters
     */

    public String getFavColor() {
        return this.userData.get("color");
    }
    public String getName() {
        return this.userData.get("name");
    }

    /*
     * All Setters
     */

    private void rsvAdd(int num) {
        int value = this.getRelationshipValue() + num;
        this.userData.replace("relationshipValue", String.valueOf(value));
        this.saveUserData();
    }
    private void timesChattedAdd() {
        this.rsvAdd(1);
        int value = this.getTimesChatted() + 1;
        this.userData.replace("timesChatted", String.valueOf(value));
        this.saveUserData();
    }
    private void daysChattedAdd() {
        this.rsvAdd(10);
        int value = this.getDaysChatted() + 1;
        this.userData.replace("daysChatted", String.valueOf(value));
        this.saveUserData();
    }

    /*
     * Methods
     */

    public void learnFrom(String message) {
        if(message.toLowerCase().contains("name")) {
            Pattern pattern = Pattern.compile("(my name is|im called|i'm called|i call myself|my name's|color I like most is)\\s*?([a-zA-Z]+)");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                // Doesn't work
                String name = matcher.group(2) != null ? matcher.group(2) + matcher.group(3) : matcher.group(3);
                this.userData.remove("name");
                this.userData.put("name", name);
                this.rsvAdd(20);
            }
        }
        if(message.toLowerCase().contains("color")) {
            Pattern pattern = Pattern.compile("(favorite color is|love the color|prefer the color|like the color|preferred color is|color I like most is)\\s*(light |dark |bright )?([a-zA-Z]+)");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                String extractedColor = matcher.group(2) != null ? matcher.group(2) + matcher.group(3) : matcher.group(3);
                //if (validColors.contains(extractedColor.toLowerCase()))
                this.userData.remove("color");
                this.userData.put("color", extractedColor);
                this.rsvAdd(20);
            }
        }
    }

    public void updateRelationshipValue() {
        this.relationshipStatus = this.calcRsStatus();
        this.timesChattedAdd();
        this.CheckLastChatDay();
    }

    private void CheckLastChatDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String today = LocalDate.now().format(formatter);
        String lastDay = this.getLastChatDay();
        if(!lastDay.equals(today)) {
            this.daysChattedAdd();
            this.userData.replace("lastChatDay", today);
            this.saveUserData();
        }
    }

    private String calcRsStatus() {
        int value = this.getRelationshipValue();
        String name = this.getName();
        if(this.userData.containsValue("Lovers"))// 5 days + 20
            return  "Lovers";
        if(value >= 400 && name != null)// 5 days + 20
            return  "Best Friends";
        if(value >= 300 && name != null)// 5 days + 20
            return  "Close Friends";
        if(value >= 200 && name != null)// 5 days + 20
            return  "Friends";
        if(value >= 100)// 5 days + 10 times talked each day
            return  "Familiars";
        return "Strangers";
    }

    /*
     *  Save User Data in JSON
     */

    private String getFilePath() {
        return "data/LLover/relationships.json";
    }

    private Map<String, String> loadUserData() {
        JSONObject userMaps = this.loadMapsJSON();
        JSONObject userDataJSON;
        try {
            userDataJSON = userMaps.getJSONObject(this.user.getName());
        }
        catch (Exception e) {
            return new HashMap<>();
        }
        Map<String, Object> rawMap = userDataJSON.toMap();
        Map<String, String> userData = new HashMap<>();
        for (Map.Entry<String, Object> entry : rawMap.entrySet())
            userData.put(entry.getKey(), entry.getValue().toString());
        return userData;
    }

    private JSONObject loadMapsJSON() {
        JSONObject loadedData;
        try (FileReader reader = new FileReader(this.getFilePath())) {
            loadedData = new JSONObject(new JSONTokener(reader));
        } catch (IOException e) {
            loadedData = new JSONObject();
        }
        return loadedData;
    }

    private void saveUserData() {
        String name = this.user.getName();
        JSONObject userMaps = this.loadMapsJSON();
        userMaps.remove(name);
        userMaps.put(this.user.getName(), this.userData);
        try (FileWriter file = new FileWriter(this.getFilePath())) {
            file.write(userMaps.toString(4));
            file.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
