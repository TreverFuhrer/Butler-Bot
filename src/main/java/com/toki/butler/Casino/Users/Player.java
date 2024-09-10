package com.toki.butler.Casino.Users;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;

public class Player {

    private final String ID;

    private double cash;
    private int betLevel;
    private LocalDate usedBrokieCmd;

    public Player(String ID, double cash) {
        this.ID = ID;
        this.cash = cash;
        this.savePlayer();
    }

    /*
     * Getters
     */
    public String getID() {
        return this.ID;
    }
    public double getCash() {
        return this.cash;
    }

    /*
     * Setters
     */
    public void setCash(double cash) {
        this.cash = cash;
        this.savePlayer();
    }
    public void addCash(double cash) {
        this.setCash(this.cash + cash);
    }
    public void removeCash(double cash) {
        this.setCash(this.cash - cash);
    }
    public void multiCash(double mul) {
        this.setCash(this.cash * mul);
    }

    /*
     * Methods
     */
    public boolean checkUsedBrokieCmd() {
        if(this.usedBrokieCmd == null)
            return false;
        return this.usedBrokieCmd.equals(LocalDate.now());
    }
    public void setUsedBrokieCmdToday() {
        this.usedBrokieCmd = LocalDate.now();
    }

    /*
     * JSON
     */
    public void savePlayer() {
        Path path = Paths.get("data/casino.json");
        try {
            // Step 1: Read the existing JSON file
            String content = Files.readString(path);
            JSONObject json = new JSONObject(content);

            // Step 2: Update or Add Player Information
            JSONObject playerJson = new JSONObject();
            playerJson.put("id", this.ID);
            playerJson.put("cash", this.cash);

            json.put(this.ID, playerJson);

            // Step 3: Write the updated JSON back to the file
            Files.writeString(path, json.toString(4), StandardOpenOption.TRUNCATE_EXISTING);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
