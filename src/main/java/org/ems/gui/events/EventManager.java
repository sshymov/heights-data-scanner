package org.ems.gui.events;

import java.util.*;

/**
 * User: stas
 * Date: Oct 1, 2008
 */
public class EventManager {
    private Map<Integer, Set<EventListener>> listeners=new HashMap<Integer, Set<EventListener>>();

    private static EventManager eventManager=new EventManager();

    public EventManager() {
    }

    public static EventManager getInstance() {
        return eventManager;
    }

    public void fireEvent(Event event) {
        Set<EventListener> subscribers=listeners.get(event.getType());
        if (subscribers==null) return;
        for (EventListener listener : subscribers) {
            if (listener.isAcceptEvent(event.getType()))
                listener.handleEvent(event.getParameters());
        }
    }

    public void register(int eventType, EventListener listener) {
        if (listeners.get(eventType)==null) {
            listeners.put(eventType,new HashSet<EventListener>());
        }
        listeners.get(eventType).add(listener);
    }

    public void unregister(int eventType, EventListener listener) {
        if (listeners.get(eventType)!=null) {
            listeners.get(eventType).remove(listener);
        }
    }

    public void unregisterAll(int eventType) {
        if (listeners.get(eventType)!=null)
            listeners.get(eventType).clear();
    }

}
