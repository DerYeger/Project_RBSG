package de.uniks.se19.team_g.project_rbsg.ingame.cells_url;

import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Cell;

import java.util.Random;

/**
 * @author  Keanu Stückrad
 */
public class ForestUrls {

    private static Random random;

    public static String getZero() {
        random = new Random();
        return random.nextBoolean() ? "zeroNeighbors.png" : "zeroNeighborsExtra.png";
    }

    public static String getOne(Cell cell) {
        String biomeLeft = BiomUrls.getLeftBiom(cell);
        String biomeRight = BiomUrls.getRightBiom(cell);
        String biomeBottom = BiomUrls.getBottomBiom(cell);
        return "Forest".equals(biomeLeft) ? "f1-1.png" :
                        "Forest".equals(biomeBottom) ? "f1-2.png" :
                                "Forest".equals(biomeRight) ? "f1-3.png" :
                                        "f1-4.png";
    }

    public static String getTwo(Cell cell) {
        random = new Random();
        String biomeLeft = BiomUrls.getLeftBiom(cell);
        String biomeRight = BiomUrls.getRightBiom(cell);
        String biomeBottom = BiomUrls.getBottomBiom(cell);
        String biomeTop = BiomUrls.getTopBiom(cell);
        return "Forest".equals(biomeLeft) && "Forest".equals(biomeRight) ? random.nextBoolean() ? "f2-1-1.png" : "f2-1-2.png" :
                        "Forest".equals(biomeLeft) && "Forest".equals(biomeBottom) ? "f2-2.png" :
                                "Forest".equals(biomeRight) && "Forest".equals(biomeBottom) ? "f2-3.png" :
                                        "Forest".equals(biomeLeft) && "Forest".equals(biomeTop) ? "f2-4.png" :
                                                "Forest".equals(biomeRight) && "Forest".equals(biomeTop) ? "f2-5.png" :
                                                        random.nextBoolean() ? "f2-6-1.png" : "f2-6-2.png";
    }

    public static String getThree(Cell cell) {
        random = new Random();
        String biomeLeft = BiomUrls.getLeftBiom(cell);
        String biomeRight = BiomUrls.getRightBiom(cell);
        String biomeBottom = BiomUrls.getBottomBiom(cell);
        String biomeTop = BiomUrls.getTopBiom(cell);
        String biomeTopRight = BiomUrls.getTopRightBiom(cell);
        String biomeTopLeft = BiomUrls.getTopLeftBiom(cell);
        String biomeBottomRight = BiomUrls.getBottomRightBiom(cell);
        String biomeBottomLeft = BiomUrls.getBottomLeftBiom(cell);
        return "Forest".equals(biomeLeft) && "Forest".equals(biomeTop) && "Forest".equals(biomeBottom) ? "f3-1.png" :
                "Forest".equals(biomeLeft) && "Forest".equals(biomeBottom) && "Forest".equals(biomeRight) ? "f3-2.png" :
                        "Forest".equals(biomeLeft) && "Forest".equals(biomeBottom) && "Forest".equals(biomeBottomLeft) ? random.nextBoolean() ? "f3-3-1.png" : "f3-3-2.png" :
                                "Forest".equals(biomeRight) && "Forest".equals(biomeBottom) && "Forest".equals(biomeBottomRight) ? random.nextBoolean() ? "f3-4-1.png" : "f3-4-2.png" :
                                        "Forest".equals(biomeLeft) && "Forest".equals(biomeRight) && "Forest".equals(biomeTop) ? "f3-5.png" :
                                                "Forest".equals(biomeLeft) && "Forest".equals(biomeTop) && "Forest".equals(biomeTopLeft) ? random.nextBoolean() ? "f3-6-1.png" : "f3-6-2.png" :
                                                        "Forest".equals(biomeTop) && "Forest".equals(biomeTopRight) && "Forest".equals(biomeRight) ? random.nextBoolean() ? "f3-7-1.png" : "f3-7-2.png" :
                                                                "f3-8.png";
    }

    public static String getFour(Cell cell) {
        random = new Random();
        String biomeLeft = BiomUrls.getLeftBiom(cell);
        String biomeRight = BiomUrls.getRightBiom(cell);
        String biomeBottom = BiomUrls.getBottomBiom(cell);
        String biomeTop = BiomUrls.getTopBiom(cell);
        String biomeTopRight = BiomUrls.getTopRightBiom(cell);
        String biomeTopLeft = BiomUrls.getTopLeftBiom(cell);
        String biomeBottomRight = BiomUrls.getBottomRightBiom(cell);
        String biomeBottomLeft = BiomUrls.getBottomLeftBiom(cell);
        return "Forest".equals(biomeLeft) && "Forest".equals(biomeTop) && "Forest".equals(biomeBottom) && "Forest".equals(biomeBottomLeft) ? random.nextBoolean() ? "f4-1-1.png" : "f4-1-2.png" :
                        "Forest".equals(biomeLeft) && "Forest".equals(biomeBottom) && "Forest".equals(biomeTopLeft) && "Forest".equals(biomeTop) ? random.nextBoolean() ? "f4-2-1.png" : "f4-2-2.png" :
                                "Forest".equals(biomeLeft) && "Forest".equals(biomeBottom) && "Forest".equals(biomeBottomLeft) && "Forest".equals(biomeRight) ? random.nextBoolean() ? "f4-3-1.png" : "f4-3-2.png" :
                                        "Forest".equals(biomeRight) && "Forest".equals(biomeBottom) && "Forest".equals(biomeBottomRight) && "Forest".equals(biomeLeft) ? random.nextBoolean() ? "f4-4-1.png" : "f4-4-2.png" :
                                                "Forest".equals(biomeLeft) && "Forest".equals(biomeRight) && "Forest".equals(biomeTop) && "Forest".equals(biomeBottom) ? "f4-5.png" :
                                                        "Forest".equals(biomeLeft) && "Forest".equals(biomeTop) && "Forest".equals(biomeTopLeft) && "Forest".equals(biomeRight) ? random.nextBoolean() ? "f4-6-1.png" : "f4-6-2.png" :
                                                                "Forest".equals(biomeTop) && "Forest".equals(biomeTopRight) && "Forest".equals(biomeRight) && "Forest".equals(biomeLeft) ? random.nextBoolean() ? "f4-7-1.png" : "f4-7-2.png" :
                                                                        "Forest".equals(biomeTop) && "Forest".equals(biomeBottomRight) && "Forest".equals(biomeRight) && "Forest".equals(biomeBottom) ? random.nextBoolean() ? "f4-8-1.png" : "f4-8-2.png" :
                                                                                random.nextBoolean() ? "f4-9-1.png" : "f4-9-2.png";
    }

    public static String getFive(Cell cell) {
        random = new Random();
        String biomeLeft = BiomUrls.getLeftBiom(cell);
        String biomeRight = BiomUrls.getRightBiom(cell);
        String biomeBottom = BiomUrls.getBottomBiom(cell);
        String biomeTop = BiomUrls.getTopBiom(cell);
        String biomeTopRight = BiomUrls.getTopRightBiom(cell);
        String biomeTopLeft = BiomUrls.getTopLeftBiom(cell);
        String biomeBottomRight = BiomUrls.getBottomRightBiom(cell);
        String biomeBottomLeft = BiomUrls.getBottomLeftBiom(cell);
        return !"Forest".equals(biomeRight) ? random.nextBoolean() ? "f5-1-1.png" : "f5-1-2.png" :
                        !"Forest".equals(biomeTop) ? random.nextBoolean() ? "f5-2-1.png" : "f5-2-2.png" :
                                !"Forest".equals(biomeTopLeft) && !"Forest".equals(biomeTopRight) && !"Forest".equals(biomeBottomRight) ? "f5-3.png" :
                                        !"Forest".equals(biomeBottomLeft) && !"Forest".equals(biomeTopLeft) && !"Forest".equals(biomeTopRight) ? "f5-4.png" :
                                                !"Forest".equals(biomeBottomLeft) && !"Forest".equals(biomeBottomRight) && !"Forest".equals(biomeTopLeft) ? "f5-5.png" :
                                                        !"Forest".equals(biomeBottom) ? random.nextBoolean() ? "f5-6-1.png" : "f5-6-2.png" :
                                                                 !"Forest".equals(biomeLeft) ? random.nextBoolean() ? "f5-7-1.png" : "f5-7-2.png" :
                                                                        "f5-8.png";
    }

    public static String getSix(Cell cell) {
        String biomeTopRight = BiomUrls.getTopRightBiom(cell);
        String biomeTopLeft = BiomUrls.getTopLeftBiom(cell);
        String biomeBottomRight = BiomUrls.getBottomRightBiom(cell);
        String biomeBottomLeft = BiomUrls.getBottomLeftBiom(cell);
        return !"Forest".equals(biomeTopRight) && !"Forest".equals(biomeBottomRight) ? "f6-1.png" :
                        !"Forest".equals(biomeTopRight) && !"Forest".equals(biomeBottomLeft) ? "f6-2.png" :
                                !"Forest".equals(biomeTopRight) && !"Forest".equals(biomeTopLeft) ? "f6-3.png" :
                                        !"Forest".equals(biomeTopLeft) && !"Forest".equals(biomeBottomRight) ? "f6-4.png" :
                                                !"Forest".equals(biomeTopLeft) && !"Forest".equals(biomeBottomLeft) ? "f6-5.png" :
                                                        "f6-6.png";
    }

    public static String getSeven(Cell cell) {
        String biomeTopRight = BiomUrls.getTopRightBiom(cell);
        String biomeTopLeft = BiomUrls.getTopLeftBiom(cell);
        String biomeBottomRight = BiomUrls.getBottomRightBiom(cell);
        return !"Forest".equals(biomeTopRight) ? "f7-1.png" :
                        !"Forest".equals(biomeTopLeft) ? "f7-2.png" :
                                !"Forest".equals(biomeBottomRight) ? "f7-3.png" :
                                        "f7-4.png";
    }

    public static String getEight() {
        return "eightNeighbors.png";
    }

}
