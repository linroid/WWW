package io.hackathon.www;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by bisunday on 6/7/15.
 */
public class GameLogic {

    private String name;
    private int score;
    private int health;
    private long lastSecs;
    private int level;
    private int numOfAdvanced;
    public final int HealthDelta = 2000;
    public final int maxLevel = 10;

    public GameLogic(String name) {
        // todo sth
        this.lastSecs = System.currentTimeMillis() / 1000;
        this.name = name;
        this.score = 0;
        this.numOfAdvanced = 0;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public boolean isRunning() {
        long current = System.currentTimeMillis() / 1000;
        if (current - lastSecs > 120) {
            return false;
        }
        return true;
    }

    public boolean isLive() {
        if (health < 0) {
            return false;
        }
        return true;
    }

    public void killmosq(int mode) {
        switch (mode) {
            case 1: score = score + 10;break;
            case 2: score = score + 50;break;
            case 3: numOfAdvanced += 1; break;
        }
    }

    public void skipmosq(int mode) {
        switch (mode) {
            case 1: health -= 20;break;
            case 2: health -= 50;break;
            case 3: health -= 100; break;
        }
    }
}
