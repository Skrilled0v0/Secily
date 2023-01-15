/*
 *MCP-919-main
 *Code by SkrilledSense
 *20230108
 */
package me.skrilled.utils.math;

public class TimerUtil {
    public long lastMS;

    public TimerUtil() {
        this.reset();
    }
//    void i(){
//
//    }


    public long getCurrentMS() {
        return System.currentTimeMillis();
    }
    public boolean hasReached(int milliseconds) {

        return (this.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

}
