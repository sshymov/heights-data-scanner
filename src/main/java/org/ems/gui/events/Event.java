package org.ems.gui.events;

import java.util.Map;

/**
 * User: stas
 * Date: Oct 1, 2008
 */
public interface Event {

    public static final int EVENT_UPDATE_STATUS=1;

    public int getType();
    public Map getParameters();
}
