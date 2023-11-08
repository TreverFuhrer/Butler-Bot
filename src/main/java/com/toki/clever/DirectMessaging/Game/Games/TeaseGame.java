package com.toki.clever.DirectMessaging.Game.Games;

import com.toki.clever.DirectMessaging.Game.Game;
import net.dv8tion.jda.api.entities.User;

import java.util.Timer;
import java.util.TimerTask;

public class TeaseGame extends Game {

    private String gender;
    private String intensity;
    private final Timer timer;

    public TeaseGame(User user) {
        super(user);
        this.timer = new Timer();
    }

    @Override
    public void run() {
        super.sendMessage("**You chose Tease Game!**");
        super.sendMessage("First I need some information...");
        super.sendMessage("**What is your gender?**\n:mens: male or :womens: female");
        // Goes to inputs
    }

    public void runGame() {
        super.sendMessage("**You can stop the game anytime\njust say stop**");
        Tease teaseObj = new Tease(this.gender);
        if(this.intensity.equals("high"))
            this.eventHigh(teaseObj,1, "", 0);
        else if(this.intensity.equals("medium"))
            this.eventMed(0, teaseObj);
        else
            this.eventLow(0, teaseObj);
    }

    public void stopGame() {
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

    // User Inputs
    public void genderInput(String gender) {
        this.gender = gender;
        super.sendMessage("Now what intensity? :candle:");
        super.sendMessage("1. High - Nonstop\n2. Medium - Every 5-15 min\n3. Low - Every 10-60min");
    }

    public void intensityInput(String intensity) {
        this.intensity = intensity;
        this.runGame();
    }

    // Events by Intensity
    public void eventHigh(Tease teaseObj, int time, String setTease,  int set) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                String tease = teaseObj.getTease();
                int userTime = (int) (Math.random() * 13) + 3;

                int newSet = set;
                String thisSetTease = "";
                if (tease.contains("Set") && !(set > 0)) {
                    thisSetTease = tease;
                    int setStart = tease.indexOf("1");
                    int setEnd = tease.lastIndexOf("1");
                    tease = tease.substring(setStart + 2, setEnd - 1);
                    newSet++;
                }
                if (set > 0) {
                    String setMarker = String.valueOf(set + 1);
                    int setStart = setTease.indexOf(setMarker);
                    int setEnd = setTease.lastIndexOf(setMarker);
                    int setNext = setTease.indexOf(String.valueOf(set + 2));
                    tease = setTease.substring(setStart + 2, setEnd - 1);
                    if (setNext != -1) {
                        thisSetTease = setTease;
                        newSet++;
                    } else {
                        thisSetTease = "";
                        newSet = 0;
                    }
                }
                String timeEmoji = (userTime > 8) ? ":hourglass_flowing_sand:" : ":hourglass:";
                sendMessage(getEmoji());
                sendMessage(tease + " **[" + userTime + " sec]** " + timeEmoji);
                eventHigh(teaseObj, userTime + 1, thisSetTease, newSet);
            }
        };
        this.timer.schedule(task, time* 1000L);
    }

    public void eventMed(int time, Tease teaseObj) {
        super.sendMessage("**You chose:** " + teaseObj.getTease());
    }

    public void eventLow(int time, Tease teaseObj) {
        super.sendMessage("**You chose:** " + teaseObj.getTease());
    }

    public String getEmoji() {
        String[] emojis = {":flushed:",":weary:",":face_with_spiral_eyes:",":blush:",":smirk:",":yum:",":innocent:",
                ":relaxed:",":face_with_hand_over_mouth:",":face_with_peeking_eye:",":confounded:",":wink:",":heart_eyes:",
                ":smiling_face_with_3_hearts:",":stuck_out_tongue_closed_eyes:",":stuck_out_tongue_winking_eye:",":stuck_out_tongue:",
                ":melting_face:",":face_with_open_eyes_and_hand_over_mouth:",":drooling_face:",":smirk_cat:",":kiss:",":lips:",
                ":biting_lip:",":tongue:",":pray::skin-tone-2:",":eyes:",":lipstick:",":high_heel:",":sweat_drops:",
                ":eggplant:",":peach:",":cherries:",":banana:",":two_hearts:",":heart_on_fire:"};
        return emojis[(int)(Math.random()* emojis.length)];
    }
}
