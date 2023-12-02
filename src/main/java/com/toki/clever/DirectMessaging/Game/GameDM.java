package com.toki.clever.DirectMessaging.Game;

import com.toki.clever.DirectMessaging.Game.Games.Tease;
import com.toki.clever.DirectMessaging.Game.Games.TeaseGame;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class GameDM extends ListenerAdapter {
    private enum State {
        NONE, GAME_SELECTION, MODDING_GAME, RUNNING_GAME
    }

    private final HashMap<User, Game> games = new HashMap<>();
    private final HashMap<User, State> states = new HashMap<>();

    public void onMessageReceived(MessageReceivedEvent event)
    {
        String message = event.getMessage().getContentRaw();
        if(!event.isFromType(ChannelType.PRIVATE)) return;
        User user = event.getAuthor();
        if(user.isBot()) return;

        this.states.putIfAbsent(user, State.NONE);
        State state = this.states.get(user);

        switch (state) {
            case NONE -> this.noState(user, message);
            case GAME_SELECTION -> this.selectGame(user, message);
            case MODDING_GAME -> this.modGame(user, message);
            case RUNNING_GAME -> this.runningGame(user, message);
        }

        // Modding Game
        if(message.equalsIgnoreCase("mod game")) {
            user.openPrivateChannel().queue(privateChannel ->
                    privateChannel.sendMessage("""
                                **Which game do you want to mod?**
                                   *Copy Example-*
                                Tease Game: Add or Remove (Tease)[male or female]
                                """).queue());
            this.states.replace(user,State.MODDING_GAME);
        }

    }

    public void noState(User user, String message) {
        if((message.contains("play") || message.contains("game"))) {
            user.openPrivateChannel().queue(privateChannel ->
                    privateChannel.sendMessage("""
                                :page_facing_up: **Games:** Tease Game,
                                **Say the game's name**
                                """).queue());
            this.states.replace(user,State.GAME_SELECTION);
            System.out.println(user.getName() + " started a game");
        }
    }

    public void selectGame(User user, String message) {
        if(message.contains("Tease") || message.contains("tease")) {
            this.games.put(user, new TeaseGame(user));
            this.states.replace(user,State.RUNNING_GAME);
        }
    }

    public void modGame(User user, String message) {
        if(message.contains("Tease Game")) {
            String tease = message.substring(message.indexOf('(')+1,message.lastIndexOf(')'));
            String gender = message.substring(message.indexOf('[')+1,message.indexOf(']'));
            if(!(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"))) {
                user.openPrivateChannel().queue(privateChannel ->
                        privateChannel.sendMessage("Invalid").queue());
                return;
            }
            if(message.contains("add") || message.contains("Add")) {
                Tease.addTease(tease, gender);
                this.states.replace(user,this.checkGameState(user));
                return;
            }
            if(message.contains("remove") || message.contains("Remove")) {
                Tease.removeTease(tease, gender);
                this.states.replace(user,this.checkGameState(user));
                return;
            }
        }
        if(message.equalsIgnoreCase("Another Game")) {
            System.out.println("Another game");
        }
        if(message.equalsIgnoreCase("Stop")) {
            user.openPrivateChannel().queue(privateChannel ->
                    privateChannel.sendMessage("Stopping Modification.").queue());
            this.states.replace(user,this.checkGameState(user));
        }
    }

    public void runningGame(User user, String message) {
        Game userGame = this.games.get(user);
        // Checks if user has a running game
        if(userGame != null) {
            // Stop Command
            if (message.equalsIgnoreCase("stop")) {
                user.openPrivateChannel().queue(privateChannel ->
                        privateChannel.sendMessage("Stopping Game :)").queue());
                userGame.stopGame();
                this.games.remove(user);
                this.states.replace(user,State.NONE);
                System.out.println(user.getName() + " stopped their game");
                return;
            }
            // Get User Input For Tease Game
            if (userGame instanceof TeaseGame) {
                if (message.equalsIgnoreCase("male"))
                    ((TeaseGame) userGame).genderInput("male");
                else if (message.equalsIgnoreCase("female"))
                    ((TeaseGame) userGame).genderInput("female");
                if (message.equalsIgnoreCase("high"))
                    ((TeaseGame) userGame).intensityInput("high");
                else if (message.equalsIgnoreCase("medium"))
                    ((TeaseGame) userGame).intensityInput("medium");
                else if (message.equalsIgnoreCase("low"))
                    ((TeaseGame) userGame).intensityInput("low");
            }
        }
    }

    public State checkGameState(User user) {
        Game userGame = this.games.get(user);
        if(userGame != null)
            return State.RUNNING_GAME;
        return State.NONE;
    }

}
