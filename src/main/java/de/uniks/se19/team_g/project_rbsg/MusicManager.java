package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * @author Keanu Stückrad
 */
@Component
public class MusicManager extends JavaFXUtils {

    private URL musicNoteBlack = getClass().getResource("/assets/icons/navigation/music-note-black.png");
    private URL musicNoteWhite = getClass().getResource("/assets/icons/navigation/music-note-white.png");
    private URL musicNoteBlackOff = getClass().getResource("/assets/icons/navigation/music-off-black.png");
    private URL musicNoteWhiteOff = getClass().getResource("/assets/icons/navigation/music-off-white.png");
    private URL music = getClass().getResource("/assets/music/simple-8bit-loop.mp3");

    private AudioClip audioClip = new AudioClip(music.toString());
    private static final int ICON_SIZE = 40;
    private boolean musicRunning = true;

    public MusicManager init() {
        audioClip.setCycleCount(AudioClip.INDEFINITE);
        return this;
    }

    public void initMusic() {
        audioClip.play();
    }

    public void initButtonIcons(Button soundButton) {
        if(musicRunning) {
            setButtonIcons(soundButton, musicNoteBlack, musicNoteWhite, ICON_SIZE);
        } else {
            setButtonIcons(soundButton, musicNoteBlackOff, musicNoteWhiteOff, ICON_SIZE);
        }
    }

    public void updateMusicButtonIcons(Button soundButton) {
        musicRunning = !musicRunning;
        if(musicRunning) {
            audioClip.play();
            setButtonIcons(soundButton, musicNoteBlack, musicNoteWhite, ICON_SIZE);
        } else {
            audioClip.stop();
            setButtonIcons(soundButton, musicNoteBlackOff, musicNoteWhiteOff, ICON_SIZE);
        }
    }

}
