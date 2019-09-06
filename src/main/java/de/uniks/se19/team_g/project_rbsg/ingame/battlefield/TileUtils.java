package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.*;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.cells_url.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import javafx.scene.image.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Keanu Stückrad
 * @author Georg Siebert
 */

public class TileUtils
{

    private static Image grass = new Image("/assets/cells/grass/grass1.png");
    private static Logger logger = LoggerFactory.getLogger(TileUtils.class);
    public static Image getBackgroundImage(Cell cell)
    {
        Random random = new Random();
        if (cell.getBiome().toString().equals("Grass"))
        {
            int grassType = random.nextInt(5) + 1;
            return new Image("/assets/cells/grass/grass" + grassType + ".png");
        }

        String biome = cell.getBiome().toString().equals("Forest") ? "forest" :
                cell.getBiome().toString().equals("Water") ? "water" : "mountain";
        int neighborSize = countNeighbors(cell);
        switch (neighborSize)
        {
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

    private static Image getImageNeighborsEight(Cell cell, String biome)
    {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getEight() :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getEight() : MountainUrls.getEight();
        return new Image("/assets/cells/" + biome + "/" + png);
    }

    private static Image getImageNeighborsSeven(Cell cell, String biome)
    {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getSeven(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getSeven(cell) : MountainUrls.getSeven(cell);
        return new Image("/assets/cells/" + biome + "/seven_neighbors/" + png);
    }

    private static Image getImageNeighborsSix(Cell cell, String biome)
    {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getSix(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getSix(cell) : MountainUrls.getSix(cell);
        return new Image("/assets/cells/" + biome + "/six_neighbors/" + png);
    }

    private static Image getImageNeighborsFive(Cell cell, String biome)
    {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getFive(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getFive(cell) : MountainUrls.getFive(cell);
        return new Image("/assets/cells/" + biome + "/five_neighbors/" + png);
    }

    private static Image getImageNeighborsFour(Cell cell, String biome)
    {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getFour(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getFour(cell) : MountainUrls.getFour(cell);
        return new Image("/assets/cells/" + biome + "/four_neighbors/" + png);
    }

    private static Image getImageNeighborsThree(Cell cell, String biome)
    {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getThree(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getThree(cell) : MountainUrls.getThree(cell);
        return new Image("/assets/cells/" + biome + "/three_neighbors/" + png);
    }

    private static Image getImageNeighborsTwo(Cell cell, String biome)
    {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getTwo(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getTwo(cell) : MountainUrls.getTwo(cell);
        return new Image("/assets/cells/" + biome + "/two_neighbors/" + png);
    }

    private static Image getImageNeighborOne(Cell cell, String biome)
    {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getOne(cell) :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getOne(cell) : MountainUrls.getOne(cell);
        return new Image("/assets/cells/" + biome + "/one_neighbor/" + png);
    }

    private static Image getImageNeighborsZero(Cell cell, String biome)
    {
        String png = cell.getBiome().toString().equals("Forest") ? ForestUrls.getZero() :
                cell.getBiome().toString().equals("Water") ? WaterUrls.getZero() : MountainUrls.getZero();
        return new Image("/assets/cells/" + biome + "/" + png);
    }

    private static int countNeighbors(Cell cell)
    {
        String biome = cell.getBiome().toString();
        boolean isBiomeLeft = biome.equals(BiomUrls.getLeftBiom(cell));
        boolean isBiomeRight = biome.equals(BiomUrls.getRightBiom(cell));
        boolean isBiomeTop = biome.equals(BiomUrls.getTopBiom(cell));
        boolean isBiomeBottom = biome.equals(BiomUrls.getBottomBiom(cell));
        int size = isBiomeLeft ? 1 : 0;
        size += isBiomeRight ? 1 : 0;
        size += isBiomeTop ? 1 : 0;
        size += isBiomeBottom ? 1 : 0;
        if (isBiomeLeft && isBiomeBottom)
        {
            size += biome.equals(BiomUrls.getBottomLeftBiom(cell)) ? 1 : 0;
        }
        if (isBiomeRight && isBiomeBottom)
        {
            size += biome.equals(BiomUrls.getBottomRightBiom(cell)) ? 1 : 0;
        }
        if (isBiomeLeft && isBiomeTop)
        {
            size += biome.equals(BiomUrls.getTopLeftBiom(cell)) ? 1 : 0;
        }
        if (isBiomeRight && isBiomeTop)
        {
            size += biome.equals(BiomUrls.getTopRightBiom(cell)) ? 1 : 0;
        }
        return size;
    }

    public static Image getDecoratorImage()
    {
        Random random = new Random();
        int decoratorSelector = random.nextInt(40);
//        logger.debug("Decorator selector: " + decoratorSelector);
        switch (decoratorSelector)
        {
            case 0:
                int flowerSelector = random.nextInt(4);
                return new Image("/assets/cells/flowers/flower" + flowerSelector + ".png",
                                 64, 64, false, true);
            case 1:
                int treeStumpSelector = random.nextInt(3);
                return new Image("/assets/cells/tree_stumps/treeStumps" + treeStumpSelector + ".png",
                                 64, 64, false, true);
            case 2:
                int boneSelector = random.nextInt(4);
                return new Image("/assets/cells/bones/bone" + boneSelector + ".png",
                                 64, 64, false, true);
            case 3:
                int poopSelector = random.nextInt(7);
                return new Image("/assets/cells/excrement/excrement" + poopSelector + ".png",
                                 64, 64, false, true);
            case 4:
                int mushroomSelector = random.nextInt(20);
                return new Image("/assets/cells/mushrooms/mushroom" + mushroomSelector + ".png",
                                 64, 64, false, true);
            case 5:
                int rockSelector = random.nextInt(6);
                return new Image("/assets/cells/small_rocks/smallRocks" + rockSelector + ".png",
                                 64, 64, false, true);
            case 6:
                int stalagmitesSelector = random.nextInt(4);
                return new Image("/assets/cells/stalagmites/stalagmites" + stalagmitesSelector + ".png",
                                 64, 64, false, true);
            case 7:
                int twigsSelector = random.nextInt(25) + 1;
                return new Image("/assets/cells/twigs/twigs" + twigsSelector + ".png",
                                 64, 64, false, true);
            default:
                return null;
        }
    }

}
