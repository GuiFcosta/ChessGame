package pt.isec.pa.chess.ui.res;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SoundManager {
    private static MediaPlayer mp;

    private SoundManager() {
    }

    public static boolean play(String fileName) {
        try {
            var url = SoundManager.class.getResource("sounds/" + fileName);
            if (url == null)
                return false;

            Media music = new Media(url.toExternalForm());

            stop();

            mp = new MediaPlayer(music);
            mp.setStartTime(Duration.ZERO);
            mp.setStopTime(music.getDuration());
            mp.setAutoPlay(true);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static void playSequence(List<String> fileNames) {
        playSequenceHelper(fileNames, 0);
    }

    private static void playSequenceHelper(List<String> fileNames, int index) {
        if (mp != null) {
            mp.dispose();
            mp = null;
        }

        if (index >= fileNames.size())
            return;

        var url = SoundManager.class.getResource("sounds/" + fileNames.get(index));
        if (url == null) {
            playSequenceHelper(fileNames, index + 1);
            return;
        }

        mp = new MediaPlayer(new Media(url.toExternalForm()));
        mp.setOnEndOfMedia(() -> playSequenceHelper(fileNames, index + 1));
        mp.play();
    }

    public static boolean isPlaying() {
        return mp != null && mp.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public static void stop() {
        if (mp != null && mp.getStatus() == MediaPlayer.Status.PLAYING)
            mp.stop();
    }

    public static List<String> getSoundList() {
        File soundsDir = new File(SoundManager.class.getResource("sounds/").getFile());

        return Arrays.stream(soundsDir.listFiles()).map(x -> x.getName()).toList();
    }
}
