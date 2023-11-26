package com.toki.clever.LLover.WakeUpDM;

import com.toki.clever.webscraper.BunnyScraper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DailyDM extends ListenerAdapter {
    public static void Daily(JDA jda) {
        String filePath = "data/dailyUsers.json";
        JSONObject usersObj = DailyDM.loadUsers(filePath);

        Iterator<String> keys = usersObj.keys();
        if(!keys.hasNext()) {
            System.out.println("No Users.");
            return;
        }
        while(keys.hasNext()) {
            String key = keys.next();
            String ID = usersObj.getString(key);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 14);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            if (calendar.getTime().before(new Date())) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {

                    // Workout
                    EmbedBuilder workoutEmbed = new EmbedBuilder();
                    workoutEmbed.setDescription("This is the workout Embed!");

                    // Main Message
                    ArrayList<EmbedBuilder> embeds = new ArrayList<>();
                    int startTime = 9, endTime = 15;
                    for(int i = startTime; i <= endTime; i++){
                        int time = (i>12) ? i-12 : i;
                        String prefix = (i>12) ? "PM" : "AM";
                        embeds.add(new EmbedBuilder().setDescription("**" + time + ":00" + prefix + " -: "));
                    }

                    // Upload Message
                    boolean isNewUpload = BunnyScraper.isNewUpload();


                    // Sends the messages
                    jda.retrieveUserById(ID).queue(user ->
                            user.openPrivateChannel().queue(privateChannel -> {
                                //this.sendMessage(privateChannel, workoutEmbed);
                                //for(EmbedBuilder embed : embeds) {
                                //    this.sendMessage(privateChannel, embed);
                                //}
                                this.sendMessage(privateChannel, "Good morning master :3");
                                if(isNewUpload)
                                    this.sendMessage(privateChannel, "Bunny Uploaded a new video!");
                            }));

                }
                private void sendMessage(PrivateChannel privateChannel, EmbedBuilder embed){
                    privateChannel.sendMessageEmbeds(embed.build()).queue();
                }
                private void sendMessage(PrivateChannel privateChannel, String message){
                    privateChannel.sendMessage(message).queue();
                }
            }, calendar.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        }
    }


    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        if (user.isBot())
            return;
        if (!event.isFromType(ChannelType.PRIVATE))
            return;
        if (!event.getMessage().getContentRaw().equalsIgnoreCase("Start Daily"))
            return;

        System.out.println("Message: " + event.getMessage().getContentRaw());

        String filePath = "data/dailyUsers.json";
        JSONObject usersObj = DailyDM.loadUsers(filePath);

        // Check if user exists
        boolean userExists = usersObj.keys().hasNext();
        // If user doesn't exist, add to JSON array
        if (!userExists) {
            // Sends User Message
            user.openPrivateChannel().queue(privateChannel ->
                    privateChannel.sendMessage("Daily Message Started.").queue());
            // Adds User to Json File
            usersObj.put(user.getName(), user.getId());
            try (FileWriter file = new FileWriter(filePath)) {
                file.write(usersObj.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            user.openPrivateChannel().queue(privateChannel ->
                    privateChannel.sendMessage("Daily Message Started.").queue());
        }
        JDA jda = event.getJDA();
        DailyDM.Daily(jda);
    }

    private static JSONObject loadUsers(String filePath) {
        JSONObject obj = new JSONObject();
        Path path = Paths.get(filePath);
        try {
            if (Files.size(path) > 0) {
                String content = new String(Files.readAllBytes(path));
                obj = new JSONObject(content);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return obj;
    }

}
