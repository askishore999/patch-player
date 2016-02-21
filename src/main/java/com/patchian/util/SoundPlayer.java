package com.patchian.util;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.SourceDataLine;

import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

public class SoundPlayer {

    public SoundPlayer() {
        System.out.println("test2");
    }

    public void play(String filePath) {
        MediaPlayerFactory factory = new MediaPlayerFactory();
        // AudioMediaPlayerComponent mediaPlayerComponent = new
        // AudioMediaPlayerComponent();
        System.out.println("test3");
        MediaList playlist = factory.newMediaList();
        System.out.println("test4");
        playlist.addMedia(filePath);
        System.out.println("test5");

        MediaListPlayer mLp = factory.newMediaListPlayer();
        System.out.println("test6");
        mLp.setMediaList(playlist);
        System.out.println("test7");
        mLp.play();
        System.out.println("test8");
    }

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();

        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private void stream(AudioInputStream in, SourceDataLine line) throws IOException {
        final byte[] buffer = new byte[4096];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            line.write(buffer, 0, n);
        }
    }
}
