package com.toki.clever.LLover.IdeasNoteApp;

public class Note {

    private String content;

    public Note(String text) {
        this.content = text;
    }

    public String getContent() {
        return this.content;
    }

    public void changeContent(String newContent) {
        this.content = newContent;
    }
}
