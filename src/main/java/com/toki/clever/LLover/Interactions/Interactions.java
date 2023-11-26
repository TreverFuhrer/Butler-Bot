package com.toki.clever.LLover.Interactions;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

public class Interactions extends ListenerAdapter {

    Map<User,InteractionState> conversationStates = new HashMap<>();
    Map<User,Relationship> relationships = new HashMap<>();

    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(!event.isFromType(ChannelType.PRIVATE)) return;
        User user = event.getAuthor();
        if(user.isBot()) return;

        this.conversationStates.putIfAbsent(user, new InteractionState(user));
        InteractionState state = this.conversationStates.get(user);
        this.relationships.putIfAbsent(user, new Relationship(user));
        Relationship relationship = this.relationships.get(user);

        String message = event.getMessage().getContentRaw();
        String topic = detectTopic(state, message);
        state.setCurrentTopic(topic);

        relationship.updateRelationshipValue();
        relationship.learnFrom(message);

        switch (state.getCurrentTopic()) {
            case "greeting": handleGreeting(user, message);
        }
    }

    /**
     * Detects if the current message is a different topic
     * @param state Users conversation state
     * @param message Users retrieved message
     * @return The current detected topic
     */
    public String detectTopic(InteractionState state, String message) {
        if (message.matches(".*(hi|hello|howdy|hey).*"))
            return "greeting";
        if (message.matches(".*(weather|temperature|rain|sunny).*"))
            return "weather";
        return state.getCurrentTopic();
    }

    public void handleGreeting(User user, String message) {
        Relationship rs = this.relationships.get(user);
        String status = rs.getRelationshipStatus();

        String name = rs.getName();
        int n;
        if(name != null)
            n = (int)(Math.random() * 2) + 1;
        else
            n = 1;

        String greeting;
        int ran = (int)(Math.random() * 3) + 1;
        if(ran == 1)
            greeting = message;
        else if(ran == 2 && n == 2)
            greeting = this.getRanGreeting(status, " " + name);
        else if(ran == 2)
            greeting = this.getRanGreeting(status, "");
        else if(ran == 3 && n == 2) {
            greeting = this.getRanGreeting(status, " " + name);
            greeting += ", how are you?";
        }
        else {
            greeting = this.getRanGreeting(status, "");
            greeting += ", how are you?";
        }
        this.sendMessage(user, greeting);
    }

    public void handleUserQuestion(User user, String message) {

    }

    /*
     * Helper Methods
     */

    /**
     * Sends a private message to a user
     * @param user Who to send message to
     * @param message The message to send
     */
    private void sendMessage(User user, String message){
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(message).queue());
    }

    /**
     * Gets a random greeting message based on relationship status and user's name
     * @param status The relationship status between LLover and user
     * @param name The users recorded name
     * @return A created message with random greeting word
     */
    public String getRanGreeting(String status, String name) {
        List<String> strangers = Arrays.asList("hello", "hi", "hey", "howdy");
        List<String> familiars = new ArrayList<>(strangers);
        familiars.addAll(Arrays.asList("sup", "yo"));
        List<String> friends = new ArrayList<>(familiars);
        friends.addAll(Arrays.asList("hiiii", "heyyy", "hellow", "ello"));
        List<String> closeFriends = new ArrayList<>(friends);
        closeFriends.addAll(Arrays.asList("*smiles* hey", ":D hi", "Carpe diem!"));
        List<String> bestFriends = new ArrayList<>(closeFriends);
        bestFriends.addAll(Arrays.asList("*hugs* hi", "*fist bumps* sup", ";) hello", "Live, laugh, love!"));
        List<String> lovers = new ArrayList<>(bestFriends);
        lovers.addAll(Arrays.asList("hi my love", "hello darling", "i missed you", "hi my lover", "*kisses* hi"));

        Random random = new Random();
        String greeting = "Unknown Status";
        switch (status) {
            case "Lovers" -> greeting = lovers.get(random.nextInt(lovers.size())) + name;
            case "Best Friends" -> greeting = bestFriends.get(random.nextInt(bestFriends.size())) + name;
            case "Close Friends" -> greeting = closeFriends.get(random.nextInt(closeFriends.size())) + name;
            case "Friends" -> greeting = friends.get(random.nextInt(friends.size())) + name;
            case "Familiars" -> greeting = familiars.get(random.nextInt(familiars.size())) + name;
            case "Strangers" -> greeting = strangers.get(random.nextInt(strangers.size())) + name;
        }
        return greeting;
    }
}
