package com.toki.clever.DirectMessaging.Game.Games;

import org.json.JSONArray;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Tease {
    private final ArrayList<String> generalTeases;
    private final ArrayList<String> sensualTeases;
    private final ArrayList<String> genderedTeases;


    public Tease(String gender) {
        this.generalTeases = Tease.loadTeases("general");
        this.sensualTeases = Tease.loadTeases("sensual");
        this.genderedTeases = Tease.loadTeases(gender);
    }

    public String getTease() {
        int num = (int)(Math.random()*5);
        if(num > 2)
            return this.getGenTease();
        else if(num > 0)
            return this.getSenTease();
        else
            return this.getGenderTease();
    }

    public String getSenGenderTease() {
        int num = (int)(Math.random()*1);
        return (num==1) ? this.getSenTease() : this.getGenderTease();
    }

    public String getGenTease() {
        int num = (int)(Math.random()*this.generalTeases.size());
        return this.generalTeases.get(num);
    }
    public String getSenTease() {
        int num = (int)(Math.random()*this.sensualTeases.size());
        return this.sensualTeases.get(num);
    }

    public String getGenderTease() {
        int num = (int)(Math.random()*this.genderedTeases.size());
        return this.genderedTeases.get(num);
    }

    // STATIC
    public static ArrayList<String> loadTeases(String fileName) {
        String filePath = "data/teases/" + fileName + "Teases.json";
        ArrayList<String> list = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        } catch (Exception e) {
            System.out.println("Loading " + fileName + "Teases Array Failed");
        }
        return list;
    }

    public static void saveTeases(ArrayList<String> list, String fileName) {
        String filePath = "data/teases/" + fileName + "Teases.json";
        JSONArray jsonArray = new JSONArray(list);

        try {
            Files.write(Paths.get(filePath), jsonArray.toString().getBytes());
        } catch (Exception e) {
            System.out.println("Saving " + fileName + "Teases Array Failed");
        }
    }

    public static void addTease(String tease, String fileName) {
        ArrayList<String> list = Tease.loadTeases(fileName);
        list.add(tease);
        Tease.saveTeases(list, fileName);
    }

    public static void removeTease(String tease, String fileName) {
        ArrayList<String> list = Tease.loadTeases(fileName);
        list.remove(tease);
        Tease.saveTeases(list, fileName);
    }

    public static String teasesToString(String fileName) {
        StringBuilder str = new StringBuilder();
        ArrayList<String> teases = Tease.loadTeases(fileName);
        for(String tease : teases) {
            str.append(tease).append(", ");
        }
        return str.substring(0,str.length()-2);
    }

}
