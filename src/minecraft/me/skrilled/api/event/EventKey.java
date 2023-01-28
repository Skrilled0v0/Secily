/*
 *eclipse
 *Code by SkrilledSense
 *20230105
 */
package me.skrilled.api.event;

import com.darkmagician6.eventapi.events.Event;

public class EventKey implements Event {
    final int key;


    public EventKey(int key) {
        this.key = key;
    }

    /**
     * 获取
     *
     * @return key
     */
    public int getKey() {
        return key;
    }


}
