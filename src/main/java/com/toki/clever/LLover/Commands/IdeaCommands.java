package com.toki.clever.LLover.Commands;

import net.dv8tion.jda.api.entities.User;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class IdeaCommands {
    private User user;

    public IdeaCommands(User user) {
        this.user = user;
    }

    // Commands
    public void ideas(String[] args) {
        for(int i = 1; i < 5; ++i) {

        }
    }

    // JSON Methods

    public String filePath() {
        return "data/ideaNotesData.json";
    }

    public JSONObject loadJSON() {
        JSONObject ideasObj = new JSONObject();
        return ideasObj;
    }

    public void saveJSON(JSONObject newSave) {
        try (FileWriter file = new FileWriter(this.filePath())){
            file.write(newSave.toString(4));
            file.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
