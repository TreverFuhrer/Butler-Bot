package com.toki.clever.webscraper;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BunnyScraper {
    public static boolean isNewUpload() {
        try {
            Document doc = Jsoup.connect("https://www.pornhub.com/model/bunnyortega/videos").get();
            Element div = doc.getElementsByClass("showingInfo").get(0);

            String infoBar = div.text();
            String uploadsText = infoBar.substring(infoBar.indexOf("f")+2);
            int currentUploads = Integer.parseInt(uploadsText);

            String filePath = "data/bunnyUploadCount.json";
            JSONObject usersObj = BunnyScraper.loadUsers(filePath);
            JSONObject newSave = new JSONObject();

            // Compare old upload count to current count
            boolean isNewUpload = false;
            if(usersObj.getInt("Uploads") == currentUploads) {
                newSave = usersObj;
            }
            else {
                newSave.put("Uploads", currentUploads);
                isNewUpload = true;
            }

            // Write upload count to JSON
            try (FileWriter file = new FileWriter(filePath)) {
                file.write(newSave.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return isNewUpload;
        }
        catch(Exception e) {
            System.out.println("isNewUpload failed.");
            return false;
        }
    }

    private static JSONObject loadUsers(String filePath) {
        JSONObject obj = new JSONObject();
        Path path = Paths.get(filePath);
        try {
            if (Files.size(path) > 0) {
                String content = new String(Files.readAllBytes(path));
                obj = new JSONObject(content);
            }
            else {
                obj.put("Uploads", -1);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return obj;
    }
}
