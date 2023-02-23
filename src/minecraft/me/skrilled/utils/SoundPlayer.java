package me.skrilled.utils;

import sun.audio.AudioPlayer;

import java.io.InputStream;

public class SoundPlayer {

    private InputStream inputStream;

    public SoundPlayer(String inputStream) {
        this.inputStream = this.getClass().getResourceAsStream(inputStream);
    }
    public void play(){
        AudioPlayer.player.start(this.inputStream);
    }
}
