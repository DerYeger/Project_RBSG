package de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class SaveFileStrategy {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    String fileName = "armies.json";

    public SaveFileStrategy() {
    }

    @Nonnull
    public File getSaveFile() throws IOException {
        String osType = System.getProperty("os.name");
        logger.debug("We are on " + osType);

        File file;

        if (osType.contains("Windows")) {
            file = getSaveFileInAppdata();
        } else {
            file = getSaveFileInHome();
        }

        return file;
    }

    @Nonnull
    File getSaveFileInHome() {
        logger.debug("Return file in home path");
        return new File(System.getProperty("user.home") + "/.local/" + getRelativeFileName());
    }

    File getSaveFileInAppdata() throws IOException {
        String appData = System.getenv("APPDATA");
        if (appData == null) {
            logger.debug("No app data found");
            return getSaveFileInHome();
            // do we need to hide the folder in this case?
            // Files.setAttribute(Paths.get(file.getPath()), "dos:hidden", true);
        }
        logger.debug("Return file in appdata path");
        return Path.of(appData, getRelativeFileName()).toFile();
    }

    /**
     * maybe prefix the save file per user so that the save game of one user doesn't overwrite the savegame of another
     *
     * @return
     */
    String getRelativeFileName() {
        return Paths.get("rbsg/", fileName).toString();
    }

    public void setFilename(String fileName) {
        this.fileName = fileName;
    }
}