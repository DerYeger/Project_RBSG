package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.cells_url;

import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.Cell;
import org.springframework.lang.NonNull;

import java.util.Random;

/**
 * @author  Keanu Stückrad
 */
public class BiomUrls {

    public static String getLeftBiom(@NonNull Cell cell) {
        return cell.getLeft() == null ? "indexOutOfBounds" : cell.getLeft().getBiome().toString();
    }

    public static String getRightBiom(@NonNull Cell cell) {
        return cell.getRight() == null ? "indexOutOfBounds" : cell.getRight().getBiome().toString();
    }

    public static String getTopBiom(@NonNull Cell cell) {
        return cell.getTop() == null ? "indexOutOfBounds" : cell.getTop().getBiome().toString();
    }

    public static String getBottomBiom(@NonNull Cell cell) {
        return cell.getBottom() == null ? "indexOutOfBounds" : cell.getBottom().getBiome().toString();
    }

    public static String getTopRightBiom(@NonNull Cell cell) {
        return cell.getTopRight() == null ? "indexOutOfBounds" : cell.getTopRight().getBiome().toString();
    }

    public static String getTopLeftBiom(@NonNull Cell cell) {
        return cell.getTopLeft() == null ? "indexOutOfBounds" : cell.getTopLeft().getBiome().toString();
    }

    public static String getBottomRightBiom(@NonNull Cell cell) {
        return cell.getBottomRight() == null ? "indexOutOfBounds" : cell.getBottomRight().getBiome().toString();
    }

    public static String getBottomLeftBiom(@NonNull Cell cell) {
        return cell.getBottomLeft() == null ? "indexOutOfBounds" : cell.getBottomLeft().getBiome().toString();
    }

    public static String get(String s1, String s2, String s3) {
        Random random = new Random();
        int i = random.nextInt(3);
        return i == 0 ? s1 : i == 1 ? s2 : s3;
    }

}
