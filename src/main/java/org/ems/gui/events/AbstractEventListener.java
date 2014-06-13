package org.ems.gui.events;

import java.util.Map;

/**
 * User: stas
 * Date: Oct 1, 2008
 */
public abstract class AbstractEventListener implements EventListener{

    private int acceptType;

    public AbstractEventListener(int acceptType) {
        this.acceptType = acceptType;
    }

    public boolean isAcceptEvent(int type) {
        return acceptType==type;
    }

    public abstract void handleEvent(Map params);

    public void register() {
        EventManager.getInstance().register(acceptType,this);
    }

    public void unregister() {
        EventManager.getInstance().unregister(acceptType,this);
    }
}
