package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Keanu Stückrad
 */
@Component
public class MusicManager {

    // https://opengameart.org/content/theme-song-8-bit

    private URL musicNoteBlack = getClass().getResource("/assets/icons/navigation/musicNoteBlack.png");
    private URL musicNoteWhite = getClass().getResource("/assets/icons/navigation/musicNoteWhite.png");
    private URL musicNoteBlackOff = getClass().getResource("/assets/icons/navigation/musicOffBlack.png");
    private URL musicNoteWhiteOff = getClass().getResource("/assets/icons/navigation/musicOffWhite.png");
    private URL opening = getClass().getResource("/assets/music/openingThemeSong.wav");
    private URL looping = getClass().getResource("/assets/music/loopingThemeSong.wav");
    private URL deathSound1 = getClass().getResource("/assets/sounds/death1.wav");
    private URL deathSound2 = getClass().getResource("/assets/sounds/death2.wav");
    private URL deathSound3 = getClass().getResource("/assets/sounds/death3.wav");

    private static final int ICON_SIZE = 40;
    public boolean musicRunning = true;

    private MediaPlayer intro;
    private MediaPlayer loop;
    private MediaPlayer deathSound;
    private boolean play = false;

    private ArrayList<URL> deathSounds;

    public MusicManager init() {
        intro = new MediaPlayer(new Media(opening.toExternalForm()));
        loop = new MediaPlayer(new Media(looping.toExternalForm()));
        intro.setCycleCount(1);
        loop.setCycleCount(AudioClip.INDEFINITE);
        intro.setOnEndOfMedia( ()-> {
            loop.play();
            play = true;
        });

        deathSounds=new ArrayList<>();
        deathSounds.add(deathSound1);
        deathSounds.add(deathSound2);
        deathSounds.add(deathSound3);

        return this;
    }

    public void initMusic() {
        intro.play();
        if(play) {
            loop.play();
        }
    }

    private void stopMusic() {
        intro.pause();
        loop.pause();
    }

    public void initButtonIcons(Button soundButton) {
        if(musicRunning) {
            JavaFXUtils.setButtonIcons(soundButton, musicNoteWhite, musicNoteBlack, ICON_SIZE);
        } else {
            JavaFXUtils.setButtonIcons(soundButton, musicNoteWhiteOff, musicNoteBlackOff, ICON_SIZE);
        }
    }

    public void toggleMusicAndUpdateButtonIconSet(Button soundButton) {

        setMusicRunning(!musicRunning);

        if(musicRunning) {
            JavaFXUtils.setButtonIcons(soundButton, musicNoteWhite, musicNoteBlack, ICON_SIZE);
        } else {
            JavaFXUtils.setButtonIcons(soundButton, musicNoteWhiteOff, musicNoteBlackOff, ICON_SIZE);
        }
    }

    public void setMusicRunning(boolean musicRunning) {
        this.musicRunning = musicRunning;

        if(musicRunning) {
            initMusic();
        } else {
            stopMusic();
        }
    }

    public Button newButton() {
        final Button musicButton = new Button();
        musicButton.getStyleClass().add("icon-button");
        musicButton.setOnAction(event -> toggleMusicAndUpdateButtonIconSet(musicButton));
        initButtonIcons(musicButton);

        return musicButton;
    }

    public void playDeathSound(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(3);
        URL randomDeathSound = deathSounds.get(randomNumber);
        deathSound = new MediaPlayer(new Media(randomDeathSound.toExternalForm()));
        deathSound.play();
    }
}
