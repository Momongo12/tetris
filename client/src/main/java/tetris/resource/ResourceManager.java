package tetris.resource;


import lombok.extern.log4j.Log4j2;


import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author denMoskvin
 * @version 1.0
 */
@Log4j2
public class ResourceManager {
    private static final String PATH_TO_ROOT_IMAGE_FOLDER = "static/img/";
    private static final String PATH_TO_ROOT_SOUNDS_FOLDER = "static/sounds/";

    public static Image getImg(String imageName) {
        try {
            return new ImageIcon(ResourceManager.class.getClassLoader().getResource(PATH_TO_ROOT_IMAGE_FOLDER + imageName)).getImage();
        } catch (Exception e) {
            log.error("Error: image {\"" + imageName + "\"} not found: ", e);
        }
        return null;
    }

    public static ImageIcon getImg(String imageName, int width, int height) {
        try {
            return new ImageIcon(getImg(imageName).getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            log.error("Error: image {\"" + imageName + "\"} not found: ", e);
        }
        return null;
    }

    public static Clip[] getAllBackgroundSounds() throws LineUnavailableException, IOException, UnsupportedAudioFileException, URISyntaxException {
        URL soundUrl = ResourceManager.class.getClassLoader().getResource(PATH_TO_ROOT_SOUNDS_FOLDER + "backgroundSounds/");
        assert soundUrl != null;
            File backgroundAudioFolder = new File(soundUrl.toURI());
            File[] audioFiles = backgroundAudioFolder.listFiles();

        assert audioFiles != null;

        Clip[] arrayOfSounds = new Clip[audioFiles.length];
        for (int i = 0; i < audioFiles.length; i++) {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFiles[i]);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            arrayOfSounds[i] = clip;
        }
        return arrayOfSounds;
    }

    public static Clip getSound(String soundTitle) throws UnsupportedAudioFileException, IOException, LineUnavailableException, URISyntaxException {
        URL soundUrl = ResourceManager.class.getClassLoader().getResource(PATH_TO_ROOT_SOUNDS_FOLDER + soundTitle);
        assert soundUrl != null;
        File file = new File(soundUrl.toURI());
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);

        return clip;
    }
}
