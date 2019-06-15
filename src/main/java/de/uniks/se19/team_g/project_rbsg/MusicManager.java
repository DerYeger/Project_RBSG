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
public class MusicManager {

    private URL musicNoteBlack = getClass().getResource("/assets/icons/navigation/musicNoteBlack.png");
    private URL musicNoteWhite = getClass().getResource("/assets/icons/navigation/musicNoteWhite.png");
    private URL musicNoteBlackOff = getClass().getResource("/assets/icons/navigation/musicOffBlack.png");
    private URL musicNoteWhiteOff = getClass().getResource("/assets/icons/navigation/musicOffWhite.png");
    private URL music = getClass().getResource("/assets/music/simple8BitLoop.mp3");

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
            JavaFXUtils.setButtonIcons(soundButton, musicNoteBlack, musicNoteWhite, ICON_SIZE);
        } else {
            JavaFXUtils.setButtonIcons(soundButton, musicNoteBlackOff, musicNoteWhiteOff, ICON_SIZE);
        }
    }

    public void updateMusicButtonIcons(Button soundButton) {
        musicRunning = !musicRunning;
        if(musicRunning) {
            audioClip.play();
            JavaFXUtils.setButtonIcons(soundButton, musicNoteBlack, musicNoteWhite, ICON_SIZE);
        } else {
            audioClip.stop();
            JavaFXUtils.setButtonIcons(soundButton, musicNoteBlackOff, musicNoteWhiteOff, ICON_SIZE);
        }
    }

}
