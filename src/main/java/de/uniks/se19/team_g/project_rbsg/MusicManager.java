package de.uniks.se19.team_g.project_rbsg;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import org.springframework.stereotype.Component;

@Component
public class MusicManager {

    private AudioClip audioClip = new AudioClip(getClass().getResource("/de/uniks/se19/team_g/project_rbsg/login/Music/simple8BitLoop.mp3").toString());
    private static final int iconSize = 40;
    private boolean musicRunning = true;

    public MusicManager init() {
        audioClip.setCycleCount(AudioClip.INDEFINITE);
        return this;
    }

    public void updateMusicButtonIcons(Button soundButton) {
        musicRunning = !musicRunning;
        if(musicRunning) {
            audioClip.play();
            setButtonIcons(soundButton, "/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_note_black_48dp.png", "/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_note_white_48dp.png");
        } else {
            audioClip.stop();
            setButtonIcons(soundButton, "/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_off_black_48dp.png", "/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_off_white_48dp.png");
        }
    }

    public void initButtonIcons(Button soundButton) {
        if(musicRunning) {
            setButtonIcons(soundButton, "/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_note_black_48dp.png", "/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_note_white_48dp.png");
        } else {
            setButtonIcons(soundButton, "/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_off_black_48dp.png", "/de/uniks/se19/team_g/project_rbsg/lobby/core/ui/Images/baseline_music_off_white_48dp.png");
        }
    }

    public void initMusic() {
        audioClip.play();
    }

    private void setButtonIcons(Button button, String hoverIconName, String nonHoverIconName) {
        ImageView hover = new ImageView();
        ImageView nonHover = new ImageView();
        nonHover.fitWidthProperty().setValue(iconSize);
        nonHover.fitHeightProperty().setValue(iconSize);
        hover.fitWidthProperty().setValue(iconSize);
        hover.fitHeightProperty().setValue(iconSize);
        hover.setImage(new Image(String.valueOf(getClass().getResource(hoverIconName))));
        nonHover.setImage(new Image(String.valueOf(getClass().getResource(nonHoverIconName))));
        button.graphicProperty().bind(Bindings.when(button.hoverProperty())
                .then(hover)
                .otherwise(nonHover));
    }

}
