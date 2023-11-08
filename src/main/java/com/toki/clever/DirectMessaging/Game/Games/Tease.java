package com.toki.clever.DirectMessaging.Game.Games;

import org.json.JSONArray;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Tease {
    private ArrayList<String> teases;

    public Tease(String gender) {
        teases = Tease.loadTeases(gender);
    }

    public String getTease() {
        int num = (int)(Math.random()*this.teases.size());
        return this.teases.get(num);
    }

    // STATIC
    public static ArrayList<String> loadTeases(String gender) {
        String filePath = "data/" + gender + "Teases.json";
        ArrayList<String> list = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        } catch (Exception e) {
            System.out.println("Loading " + gender + "Teases Array Failed");
        }
        return list;
    }

    public static void saveTeases(ArrayList<String> list, String gender) {
        String filePath = "data/" + gender + "Teases.json";
        JSONArray jsonArray = new JSONArray(list);

        try {
            Files.write(Paths.get(filePath), jsonArray.toString().getBytes());
        } catch (Exception e) {
            System.out.println("Saving " + gender + "Teases Array Failed");
        }
    }

    public static void addTease(String tease, String gender) {
        ArrayList<String> list = Tease.loadTeases(gender);
        list.add(tease);
        Tease.saveTeases(list, gender);
    }

    public static void removeTease(String tease, String gender) {
        ArrayList<String> list = Tease.loadTeases(gender);
        list.remove(tease);
        Tease.saveTeases(list, gender);
    }

    public static String teasesToString(String gender) {
        StringBuilder str = new StringBuilder();
        ArrayList<String> teases = Tease.loadTeases(gender);
        for(String tease : teases) {
            str.append(tease).append(", ");
        }
        return str.substring(0,str.length()-2);
    }

}
