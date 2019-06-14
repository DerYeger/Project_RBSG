package de.uniks.se19.team_g.project_rbsg;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import org.springframework.stereotype.Component;

/**
 * @author Keanu Stückrad
 */
@Component
public class MusicManager {

    private static final String MUSIC_NOTE_BLACK = "/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_note_black_48dp.png";
    private static final String MUSIC_NOTE_WHITE = "/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_note_white_48dp.png";
    private static final String MUSIC_NOTE_BLACK_OFF = "/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_off_black_48dp.png";
    private static final String MUSIC_NOTE_WHITE_OFF = "/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_off_white_48dp.png";
    private static final String MUSIC = "/de/uniks/se19/team_g/project_rbsg/login/Music/simple8BitLoop.mp3";

    private AudioClip audioClip = new AudioClip(getClass().getResource(MUSIC).toString());
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
            setButtonIcons(soundButton, MUSIC_NOTE_BLACK, MUSIC_NOTE_WHITE);
        } else {
            setButtonIcons(soundButton, MUSIC_NOTE_BLACK_OFF, MUSIC_NOTE_WHITE_OFF);
        }
    }

    public void updateMusicButtonIcons(Button soundButton) {
        musicRunning = !musicRunning;
        if(musicRunning) {
            audioClip.play();
            setButtonIcons(soundButton, MUSIC_NOTE_BLACK, MUSIC_NOTE_WHITE);
        } else {
            audioClip.stop();
            setButtonIcons(soundButton, MUSIC_NOTE_BLACK_OFF, MUSIC_NOTE_WHITE_OFF);
        }
    }

    private void setButtonIcons(Button button, String hoverIconName, String nonHoverIconName) {
        ImageView hover = new ImageView();
        ImageView nonHover = new ImageView();
        nonHover.fitWidthProperty().setValue(ICON_SIZE);
        nonHover.fitHeightProperty().setValue(ICON_SIZE);
        hover.fitWidthProperty().setValue(ICON_SIZE);
        hover.fitHeightProperty().setValue(ICON_SIZE);
        hover.setImage(new Image(String.valueOf(getClass().getResource(hoverIconName))));
        nonHover.setImage(new Image(String.valueOf(getClass().getResource(nonHoverIconName))));
        button.graphicProperty().bind(Bindings.when(button.hoverProperty())
                .then(hover)
                .otherwise(nonHover));
    }

}
