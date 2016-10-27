package com.example.anton.player;

/**
 * Created by Anton on 23.10.2016.
 */

public enum ButtonControlStates {
    PLAY {
        public String getText() {
            return "Play";
        }
    },
    STOP {
        public String getText() {
            return "Stop";
        }
    };
    public abstract String getText();
}
