package org.htwkvisu.utils;

import java.util.logging.Logger;

public class ActiveTimer {

    private long start;
    private boolean enable;

    public ActiveTimer() {
        start = System.currentTimeMillis();
    }

    public long stopTimer() {
        long now = System.currentTimeMillis();
        return now - start;
    }

    public void reset(){
        start = System.currentTimeMillis();
    }

    public void logInfo() {
        if(enable){
            Logger.getGlobal().info("Redraw took " + stopTimer() + " ms");
        }
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
