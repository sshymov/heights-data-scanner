package org.ems.gui.events;

import java.util.Map;
import java.util.HashMap;

/**
 * User: stas
 * Date: Oct 1, 2008
 */
public class SimpleEvent implements Event{
    private int type;
    private Map parameters=new HashMap();
    public SimpleEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public Map getParameters() {
        return parameters;
    }

    public void fire() {
        EventManager.getInstance().fireEvent(this);
    }
}
