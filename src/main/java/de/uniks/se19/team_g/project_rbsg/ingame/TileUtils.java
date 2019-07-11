package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import de.uniks.se19.team_g.project_rbsg.ingame.cells_url.BiomUrls;
import de.uniks.se19.team_g.project_rbsg.ingame.cells_url.ForestUrls;
import de.uniks.se19.team_g.project_rbsg.ingame.cells_url.MountainUrls;
import de.uniks.se19.team_g.project_rbsg.ingame.cells_url.WaterUrls;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Cell;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.UnitType;
import javafx.scene.image.Image;

import static de.uniks.se19.team_g.project_rbsg.waiting_room.model.UnitType.*;

public class TileUtils
{

    private static Image grass = new Image("/assets/cells/grass.png");

    public static Image getBackgroundImage(Cell cell) {
        if(cell.getBiome().toString().equals("Grass")) return grass;

        String biome = cell.getBiome().toString().equals("Forest") ? "forest" :
                cell.getBiome().toString().equals("Water") ? "water" : "mountain";
        int neighborSize = countNeighbors(cell);
        switch (neighborSize) {
            case 0:
                return getImageNeighborsZero(cell, biome);
            case 1:
                return getImageNeighborOne(cell, biome);
            case 2:
                return getImageNeighborsTwo(cell, biome);
            case 3:
                return getImageNeighborsThree(cell, biome);
            case 4:
                return getImageNeighborsFour(cell, biome);
            case 5:
                return getImageNeighborsFive(cell, biome);
            case 6:
                return getImageNeighborsSix(cell, biome);
            case 7:
                return getImageNeighborsSeven(cell, biome);
            case 8:
                return getImageNeighborsEight(cell, biome);
            default:
                return grass;
        }
    }

    private static Image getImageNeighborsEight(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getEight() :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getEight() : MountainUrls.getEight();
        return new Image("/assets/cells/" + biome + "/" + png);
    }

    private static Image getImageNeighborsSeven(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getSeven(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getSeven(cell) : MountainUrls.getSeven(cell);
        return new Image("/assets/cells/" + biome + "/seven_neighbors/" + png);
    }

    private static Image getImageNeighborsSix(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getSix(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getSix(cell) : MountainUrls.getSix(cell);
        return new Image("/assets/cells/" + biome + "/six_neighbors/" + png);
    }

    private static Image getImageNeighborsFive(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getFive(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getFive(cell) : MountainUrls.getFive(cell);
        return new Image("/assets/cells/" + biome + "/five_neighbors/" + png);
    }

    private static Image getImageNeighborsFour(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getFour(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getFour(cell) : MountainUrls.getFour(cell);
        return new Image("/assets/cells/" + biome + "/four_neighbors/" + png);
    }

    private static Image getImageNeighborsThree(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getThree(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getThree(cell) : MountainUrls.getThree(cell);
        return new Image("/assets/cells/" + biome + "/three_neighbors/" + png);
    }

    private static Image getImageNeighborsTwo(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getTwo(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getTwo(cell) : MountainUrls.getTwo(cell);
        return new Image("/assets/cells/" + biome + "/two_neighbors/" + png);
    }

    private static Image getImageNeighborOne(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getOne(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getOne(cell) : MountainUrls.getOne(cell);
        return new Image("/assets/cells/" + biome + "/one_neighbor/" + png);
    }

    private static Image getImageNeighborsZero(Cell cell, String biome) {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getZero() :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getZero() : MountainUrls.getZero();
        return new Image("/assets/cells/" + biome + "/" + png);
    }

    private static int countNeighbors(Cell cell) {
        String biome = cell.getBiome().toString();
        boolean isBiomeLeft = biome.equals(BiomUrls.getLeftBiom(cell));
        boolean isBiomeRight = biome.equals(BiomUrls.getRightBiom(cell));
        boolean isBiomeTop = biome.equals(BiomUrls.getTopBiom(cell));
        boolean isBiomeBottom = biome.equals(BiomUrls.getBottomBiom(cell));
        int size = isBiomeLeft ? 1 : 0;
        size += isBiomeRight ? 1 : 0;
        size += isBiomeTop ? 1 : 0;
        size += isBiomeBottom ? 1 : 0;
        if(isBiomeLeft && isBiomeBottom) {
            size += biome.equals(BiomUrls.getBottomLeftBiom(cell)) ? 1 : 0;
        }
        if(isBiomeRight && isBiomeBottom) {
            size += biome.equals(BiomUrls.getBottomRightBiom(cell)) ? 1 : 0;
        }
        if(isBiomeLeft && isBiomeTop) {
            size += biome.equals(BiomUrls.getTopLeftBiom(cell)) ? 1 : 0;
        }
        if(isBiomeRight && isBiomeTop) {
            size += biome.equals(BiomUrls.getTopRightBiom(cell)) ? 1 : 0;
        }
        return size;
    }

    public static String getUnitImagePath(UnitType unitType) {
        String imagePath;

        if(unitType.equals(INFANTRY)) imagePath = UnitTypeInfo._5cc051bd62083600017db3b6.getImage().toExternalForm();
                else if(unitType.equals(BAZOOKA_TROOPER)) imagePath = UnitTypeInfo._5cc051bd62083600017db3b7.getImage().toExternalForm();
                else if(unitType.equals(JEEP)) imagePath = UnitTypeInfo._5cc051bd62083600017db3b8.getImage().toExternalForm();
                else if(unitType.equals(LIGHT_TANK)) imagePath = UnitTypeInfo._5cc051bd62083600017db3b9.getImage().toExternalForm();
                else if(unitType.equals(HEAVY_TANK)) imagePath = UnitTypeInfo._5cc051bd62083600017db3ba.getImage().toExternalForm();
                else if(unitType.equals(CHOPPER)) imagePath = UnitTypeInfo._5cc051bd62083600017db3bb.getImage().toExternalForm();
                else imagePath = UnitTypeInfo._5cc051bd62083600017db3b8.getImage().toExternalForm();

        return imagePath;
    }

}
