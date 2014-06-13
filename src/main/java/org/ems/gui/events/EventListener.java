package org.ems.gui.events;

import java.util.Map;

/**
 * User: stas
 * Date: Oct 1, 2008
 */
public interface EventListener {
    boolean isAcceptEvent(int type);
    void handleEvent(Map params);
}
