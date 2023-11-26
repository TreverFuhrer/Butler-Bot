package com.toki.clever.LLover.Commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.function.Consumer;

public class Commands extends ListenerAdapter {

    // Hashmap of commands

    // enum of states
    // default state checks message against hashmap of commands
    // IDEA_CHANGE
    @FunctionalInterface
    interface CommandHandler {
        void handleCommand(String[] args);
    }

    private enum State {
        DEFAULT, IDEA_CHANGE
    }

    public static final HashMap<User, State> states = new HashMap<>();
    private final HashMap<String, CommandHandler> commands;
    private final HashMap<User, IdeaCommands> ideasNoteApp;

    private IdeaCommands ideaCommands;

    public Commands() {
        this.ideasNoteApp = new HashMap<>();
        this.commands = new HashMap<>();
        //this.commands.put("ideas", ideaCommands::ideas);
    }

    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(!event.isFromType(ChannelType.PRIVATE)) return;
        User user = event.getAuthor();
        if(user.isBot()) return;

        Commands.states.putIfAbsent(user, State.DEFAULT);
        this.ideasNoteApp.putIfAbsent(user, new IdeaCommands(user));
        //this.ideaCommands = this.ideasNoteApp.get(user);

        State state = Commands.states.get(user);
        switch (state) {
            case DEFAULT -> this.commands(event);
        }
    }

    public void commands(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw().toLowerCase();
        String[] parts = message.split("\\s+", 3);
        String command = parts[0] + (parts.length > 1 ? " " + parts[1] : "");
        String[] args = parts.length > 2 ? new String[] {parts[1], parts[2]} : new String[0];

        CommandHandler handler = this.commands.get(command);
        if (handler != null)
            handler.handleCommand(args);
    }

    public static void setState(User user, String state) {
        switch (state) {
            case "DEFAULT" -> Commands.states.replace(user, State.DEFAULT);
        }

    }
}
