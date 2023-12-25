package com.toki.clever.LLover.LivingGirl.Events;

import com.toki.clever.LLover.LivingGirl.Events.IdvEvents.SleepEvent;

import java.util.ArrayList;

public class EventManager {

    private ArrayList<BaseEvent> eventQueue;

    public EventManager() {
        this.eventQueue = new ArrayList<>();
        this.runEvent();
    }

    public BaseEvent getPreviousEvent() {
        return this.eventQueue.get(0);
    }
    public BaseEvent getCurrentEvent() {
        return this.eventQueue.get(1);
    }
    public BaseEvent getNextEvent() {
        return this.eventQueue.get(2);
    }

    /*
     * Methods
     */

    public void runEvent() {
        // Chooses first event to do

        this.eventQueue.add(new BaseEvent(0));
        this.eventQueue.add(new SleepEvent());
    }

    public void runNextEvent() {

    }
    // Chooses Events


}
