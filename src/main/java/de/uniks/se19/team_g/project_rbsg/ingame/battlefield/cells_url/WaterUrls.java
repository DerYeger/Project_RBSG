package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.cells_url;

import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.Cell;

import java.util.Random;

/**
 * @author  Keanu Stückrad
 */
public class WaterUrls {

    private static Random random;
    
    public static String getZero() {
        return "zeroNeighbors.png";
    }

    public static String getOne(Cell cell) {
        String biomeLeft = BiomUrls.getLeftBiom(cell);
        String biomeRight = BiomUrls.getRightBiom(cell);
        String biomeBottom = BiomUrls.getBottomBiom(cell);
        return "Water".equals(biomeLeft) ? "w1-1.png" :
                        "Water".equals(biomeBottom) ? "w1-2.png" :
                                "Water".equals(biomeRight) ? "w1-3.png" :
                                        "w1-4.png";
    }

    public static String getTwo(Cell cell) {
        String biomeLeft = BiomUrls.getLeftBiom(cell);
        String biomeRight = BiomUrls.getRightBiom(cell);
        String biomeBottom = BiomUrls.getBottomBiom(cell);
        String biomeTop = BiomUrls.getTopBiom(cell);
        return "Water".equals(biomeLeft) && "Water".equals(biomeRight) ? BiomUrls.get("w2-1-1.png", "w2-1-2.png", "w2-1-3.png") :
                    "Water".equals(biomeLeft) && "Water".equals(biomeBottom) ? "w2-2.png" :
                            "Water".equals(biomeRight) && "Water".equals(biomeBottom) ? "w2-3.png" :
                                    "Water".equals(biomeLeft) && "Water".equals(biomeTop) ? "w2-4.png" :
                                            "Water".equals(biomeRight) && "Water".equals(biomeTop) ? "w2-5.png" :
                                                    BiomUrls.get("w2-6-1.png", "w2-6-2.png", "w2-6-3.png");
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
        return "Water".equals(biomeLeft) && "Water".equals(biomeTop) && "Water".equals(biomeBottom) ? "w3-1.png" :
                    "Water".equals(biomeLeft) && "Water".equals(biomeBottom) && "Water".equals(biomeRight) ? "w3-2.png" :
                            "Water".equals(biomeLeft) && "Water".equals(biomeBottom) && "Water".equals(biomeBottomLeft) ? BiomUrls.get("w3-3-1.png", "w3-3-2.png", "w3-3-3.png") :
                                    "Water".equals(biomeRight) && "Water".equals(biomeBottom) && "Water".equals(biomeBottomRight) ? BiomUrls.get("w3-4-1.png", "w3-4-2.png", "w3-4-3.png") :
                                            "Water".equals(biomeLeft) && "Water".equals(biomeRight) && "Water".equals(biomeTop) ? "w3-5.png" :
                                                    "Water".equals(biomeLeft) && "Water".equals(biomeTop) && "Water".equals(biomeTopLeft) ? BiomUrls.get("w3-6-1.png", "w3-6-2.png", "w3-6-3.png") :
                                                            "Water".equals(biomeTop) && "Water".equals(biomeTopRight) && "Water".equals(biomeRight) ? BiomUrls.get("w3-7-1.png", "w3-7-2.png", "w3-7-3.png") :
                                                                    "w3-8.png";
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
        return "Water".equals(biomeLeft) && "Water".equals(biomeTop) && "Water".equals(biomeBottom) && "Water".equals(biomeBottomLeft) ? random.nextBoolean() ? "w4-1-1.png" : "w4-1-2.png" :
                    "Water".equals(biomeLeft) && "Water".equals(biomeBottom) && "Water".equals(biomeTopLeft) && "Water".equals(biomeTop) ? random.nextBoolean() ? "w4-2-1.png" : "w4-2-2.png" :
                            "Water".equals(biomeLeft) && "Water".equals(biomeBottom) && "Water".equals(biomeBottomLeft) && "Water".equals(biomeRight) ? random.nextBoolean() ? "w4-3-1.png" : "w4-3-2.png" :
                                    "Water".equals(biomeRight) && "Water".equals(biomeBottom) && "Water".equals(biomeBottomRight) && "Water".equals(biomeLeft) ? random.nextBoolean() ? "w4-4-1.png" : "w4-4-2.png" :
                                            "Water".equals(biomeLeft) && "Water".equals(biomeRight) && "Water".equals(biomeTop) && "Water".equals(biomeBottom) ? "w4-5.png" :
                                                    "Water".equals(biomeLeft) && "Water".equals(biomeTop) && "Water".equals(biomeTopLeft) && "Water".equals(biomeRight) ? random.nextBoolean() ? "w4-6-1.png" : "w4-6-2.png" :
                                                            "Water".equals(biomeTop) && "Water".equals(biomeTopRight) && "Water".equals(biomeRight) && "Water".equals(biomeLeft) ? random.nextBoolean() ? "w4-7-1.png" : "w4-7-2.png" :
                                                                    "Water".equals(biomeTop) && "Water".equals(biomeBottomRight) && "Water".equals(biomeRight) && "Water".equals(biomeBottom) ? random.nextBoolean() ? "w4-8-1.png" : "w4-8-2.png" :
                                                                            random.nextBoolean() ? "w4-9-1.png" : "w4-9-2.png";
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
        return !"Water".equals(biomeRight) ? BiomUrls.get("w5-1-1.png", "w5-1-2.png", "w5-1-3.png") :
                        !"Water".equals(biomeTop) ? BiomUrls.get("w5-2-1.png", "w5-2-2.png", "w5-2-3.png") :
                                !"Water".equals(biomeTopLeft) && !"Water".equals(biomeTopRight) && !"Water".equals(biomeBottomRight) ? "w5-3.png" :
                                        !"Water".equals(biomeBottomLeft) && !"Water".equals(biomeTopLeft) && !"Water".equals(biomeTopRight) ? "w5-4.png" :
                                                !"Water".equals(biomeBottomLeft) && !"Water".equals(biomeBottomRight) && !"Water".equals(biomeTopLeft) ? "w5-5.png" :
                                                        !"Water".equals(biomeBottom) ? BiomUrls.get("w5-6-1.png", "w5-6-2.png", "w5-6-3.png") :
                                                                !"Water".equals(biomeLeft) ? BiomUrls.get("w5-7-1.png", "w5-7-2.png", "w5-7-3.png") :
                                                                        "w5-8.png";
    }

    public static String getSix(Cell cell) {
        random = new Random();
        String biomeTopRight = BiomUrls.getTopRightBiom(cell);
        String biomeTopLeft = BiomUrls.getTopLeftBiom(cell);
        String biomeBottomRight = BiomUrls.getBottomRightBiom(cell);
        String biomeBottomLeft = BiomUrls.getBottomLeftBiom(cell);
        return !"Water".equals(biomeTopRight) && !"Water".equals(biomeBottomRight) ? random.nextBoolean() ? "w6-1-1.png" : "w6-1-2.png" :
                        !"Water".equals(biomeTopRight) && !"Water".equals(biomeBottomLeft) ? random.nextBoolean() ? "w6-2-1.png" : "w6-2-2.png" :
                                !"Water".equals(biomeTopRight) && !"Water".equals(biomeTopLeft) ? random.nextBoolean() ? "w6-3-1.png" : "w6-3-2.png" :
                                        !"Water".equals(biomeTopLeft) && !"Water".equals(biomeBottomRight) ? random.nextBoolean() ? "w6-4-1.png" : "w6-4-2.png" :
                                                !"Water".equals(biomeTopLeft) && !"Water".equals(biomeBottomLeft) ? random.nextBoolean() ? "w6-5-1.png" : "w6-5-2.png" :
                                                        random.nextBoolean() ? "w6-6-1.png" : "w6-6-2.png";
    }

    public static String getSeven(Cell cell) {
        random = new Random();
        String biomeTopRight = BiomUrls.getTopRightBiom(cell);
        String biomeTopLeft = BiomUrls.getTopLeftBiom(cell);
        String biomeBottomRight = BiomUrls.getBottomRightBiom(cell);
        return !"Water".equals(biomeTopRight) ? random.nextBoolean() ? "w7-1-1.png" : "w7-1-2.png" :
                        !"Water".equals(biomeTopLeft) ? random.nextBoolean() ? "w7-2-1.png" : "w7-2-2.png" :
                                !"Water".equals(biomeBottomRight) ? random.nextBoolean() ? "w7-3-1.png" : "w7-3-2.png" : random.nextBoolean() ? "w7-4-1.png" :
                                        "w7-4-2.png";
    }

    public static String getEight() {
        return "eightNeighbors.png";
    }

}
