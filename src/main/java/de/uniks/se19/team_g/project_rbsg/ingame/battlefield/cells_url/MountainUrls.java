package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.cells_url;

import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.Cell;

/**
 * @author  Keanu Stückrad
 */
public class MountainUrls {
    
    public static String getZero() {
        return "zeroNeighbors.png";
    }

    public static String getOne(Cell cell) {
        String biomeLeft = BiomUrls.getLeftBiom(cell);
        String biomeRight = BiomUrls.getRightBiom(cell);
        String biomeBottom = BiomUrls.getBottomBiom(cell);
        return "Mountain".equals(biomeLeft) ? "m1-1.png" :
                        "Mountain".equals(biomeBottom) ? "m1-2.png" :
                                "Mountain".equals(biomeRight) ? "m1-3.png" :
                                        "m1-4.png";
    }

    public static String getTwo(Cell cell) {
        String biomeLeft = BiomUrls.getLeftBiom(cell);
        String biomeRight = BiomUrls.getRightBiom(cell);
        String biomeBottom = BiomUrls.getBottomBiom(cell);
        String biomeTop = BiomUrls.getTopBiom(cell);
        return "Mountain".equals(biomeLeft) && "Mountain".equals(biomeRight) ? "m2-1.png" :
                        "Mountain".equals(biomeLeft) && "Mountain".equals(biomeBottom) ? "m2-2.png" :
                                "Mountain".equals(biomeRight) && "Mountain".equals(biomeBottom) ? "m2-3.png" :
                                        "Mountain".equals(biomeLeft) && "Mountain".equals(biomeTop) ? "m2-4.png" :
                                                "Mountain".equals(biomeRight) && "Mountain".equals(biomeTop) ? "m2-5.png" :
                                                        "m2-6.png";
    }

    public static String getThree(Cell cell) {
        String biomeLeft = BiomUrls.getLeftBiom(cell);
        String biomeRight = BiomUrls.getRightBiom(cell);
        String biomeBottom = BiomUrls.getBottomBiom(cell);
        String biomeTop = BiomUrls.getTopBiom(cell);
        String biomeTopRight = BiomUrls.getTopRightBiom(cell);
        String biomeTopLeft = BiomUrls.getTopLeftBiom(cell);
        String biomeBottomRight = BiomUrls.getBottomRightBiom(cell);
        String biomeBottomLeft = BiomUrls.getBottomLeftBiom(cell);
        return "Mountain".equals(biomeLeft) && "Mountain".equals(biomeTop) && "Mountain".equals(biomeBottom) ? "m3-1.png" :
                        "Mountain".equals(biomeLeft) && "Mountain".equals(biomeBottom) && "Mountain".equals(biomeRight) ? "m3-2.png" :
                                "Mountain".equals(biomeLeft) && "Mountain".equals(biomeBottom) && "Mountain".equals(biomeBottomLeft) ? "m3-3.png" :
                                        "Mountain".equals(biomeRight) && "Mountain".equals(biomeBottom) && "Mountain".equals(biomeBottomRight) ? "m3-4.png" :
                                                "Mountain".equals(biomeLeft) && "Mountain".equals(biomeRight) && "Mountain".equals(biomeTop) ? "m3-5.png" :
                                                        "Mountain".equals(biomeLeft) && "Mountain".equals(biomeTop) && "Mountain".equals(biomeTopLeft) ? "m3-6.png" :
                                                                "Mountain".equals(biomeTop) && "Mountain".equals(biomeTopRight) && "Mountain".equals(biomeRight) ? "m3-7.png" :
                                                                        "m3-8.png";
    }

    public static String getFour(Cell cell) {
        String biomeLeft = BiomUrls.getLeftBiom(cell);
        String biomeRight = BiomUrls.getRightBiom(cell);
        String biomeBottom = BiomUrls.getBottomBiom(cell);
        String biomeTop = BiomUrls.getTopBiom(cell);
        String biomeTopRight = BiomUrls.getTopRightBiom(cell);
        String biomeTopLeft = BiomUrls.getTopLeftBiom(cell);
        String biomeBottomRight = BiomUrls.getBottomRightBiom(cell);
        String biomeBottomLeft = BiomUrls.getBottomLeftBiom(cell);
        return "Mountain".equals(biomeLeft) && "Mountain".equals(biomeTop) && "Mountain".equals(biomeBottom) && "Mountain".equals(biomeBottomLeft) ? "m4-1.png" :
                        "Mountain".equals(biomeLeft) && "Mountain".equals(biomeBottom) && "Mountain".equals(biomeTopLeft) && "Mountain".equals(biomeTop) ? "m4-2.png" :
                                "Mountain".equals(biomeLeft) && "Mountain".equals(biomeBottom) && "Mountain".equals(biomeBottomLeft) && "Mountain".equals(biomeRight) ? "m4-3.png" :
                                        "Mountain".equals(biomeRight) && "Mountain".equals(biomeBottom) && "Mountain".equals(biomeBottomRight) && "Mountain".equals(biomeLeft) ? "m4-4.png" :
                                                "Mountain".equals(biomeLeft) && "Mountain".equals(biomeRight) && "Mountain".equals(biomeTop) && "Mountain".equals(biomeBottom) ? "m4-5.png" :
                                                        "Mountain".equals(biomeLeft) && "Mountain".equals(biomeTop) && "Mountain".equals(biomeTopLeft) && "Mountain".equals(biomeRight) ? "m4-6.png" :
                                                                "Mountain".equals(biomeTop) && "Mountain".equals(biomeTopRight) && "Mountain".equals(biomeRight) && "Mountain".equals(biomeLeft) ? "m4-7.png" :
                                                                        "Mountain".equals(biomeTop) && "Mountain".equals(biomeBottomRight) && "Mountain".equals(biomeRight) && "Mountain".equals(biomeBottom) ? "m4-8.png" :
                                                                                "m4-9.png";
    }

    public static String getFive(Cell cell) {
        String biomeLeft = BiomUrls.getLeftBiom(cell);
        String biomeRight = BiomUrls.getRightBiom(cell);
        String biomeBottom = BiomUrls.getBottomBiom(cell);
        String biomeTop = BiomUrls.getTopBiom(cell);
        String biomeTopRight = BiomUrls.getTopRightBiom(cell);
        String biomeTopLeft = BiomUrls.getTopLeftBiom(cell);
        String biomeBottomRight = BiomUrls.getBottomRightBiom(cell);
        String biomeBottomLeft = BiomUrls.getBottomLeftBiom(cell);
        return !"Mountain".equals(biomeRight) ? "m5-1.png" :
                        !"Mountain".equals(biomeTop) ? "m5-2.png" :
                                    !"Mountain".equals(biomeTopLeft) && !"Mountain".equals(biomeTopRight) && !"Mountain".equals(biomeBottomRight) ? "m5-3.png" :
                                                !"Mountain".equals(biomeBottomLeft) && !"Mountain".equals(biomeTopLeft) && !"Mountain".equals(biomeTopRight) ? "m5-4.png" :
                                                            !"Mountain".equals(biomeBottomLeft) && !"Mountain".equals(biomeBottomRight) && !"Mountain".equals(biomeTopLeft) ? "m5-5.png" :
                                                                        !"Mountain".equals(biomeBottom) ? "m5-6.png" :
                                                                                    !"Mountain".equals(biomeLeft) ? "m5-7.png" :
                                                                                                "m5-8.png";
    }

    public static String getSix(Cell cell) {
        String biomeTopRight = BiomUrls.getTopRightBiom(cell);
        String biomeTopLeft = BiomUrls.getTopLeftBiom(cell);
        String biomeBottomRight = BiomUrls.getBottomRightBiom(cell);
        String biomeBottomLeft = BiomUrls.getBottomLeftBiom(cell);
        return !"Mountain".equals(biomeTopRight) && !"Mountain".equals(biomeBottomRight) ? "m6-1.png" :
                        !"Mountain".equals(biomeTopRight) && !"Mountain".equals(biomeBottomLeft) ? "m6-2.png" :
                                    !"Mountain".equals(biomeTopRight) && !"Mountain".equals(biomeTopLeft) ? "m6-3.png" :
                                                !"Mountain".equals(biomeTopLeft) && !"Mountain".equals(biomeBottomRight) ? "m6-4.png" :
                                                            !"Mountain".equals(biomeTopLeft) && !"Mountain".equals(biomeBottomLeft) ? "m6-5.png" :
                                                                        "m6-6.png";
    }

    public static String getSeven(Cell cell) {
        String biomeTopRight = BiomUrls.getTopRightBiom(cell);
        String biomeTopLeft = BiomUrls.getTopLeftBiom(cell);
        String biomeBottomRight = BiomUrls.getBottomRightBiom(cell);
        return !"Mountain".equals(biomeTopRight) ? "m7-1.png" :
                        !"Mountain".equals(biomeTopLeft) ? "m7-2.png" :
                                !"Mountain".equals(biomeBottomRight) ? "m7-3.png" :
                                        "m7-4.png";
    }

    public static String getEight() {
        return "eightNeighbors.png";
    }

}
