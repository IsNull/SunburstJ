package controls.sunburst;

import helpers.ColorHelper;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Color scheme which returns a random color.
 * Created by n0daft on 25.04.2014.
 */
public class ColorStrategyShades implements IColorStrategy {

    // List that holds main colors.
    private ArrayList<Color> colorList = new ArrayList();

    private Map<Color, ArrayList<Color>> colorShadeMap = new HashMap<>();
    private Map<WeightedTreeItem, Color> colorMap = new HashMap<>();

    public ColorStrategyShades(){
        // Store main colors to list.
        colorList.add(Color.web("B21217")); // Dark Red
        colorList.add(Color.web("DF2126")); // Red
        colorList.add(Color.web("E98A2B")); // Orange
        colorList.add(Color.web("F6EC46")); // Yellow
        colorList.add(Color.web("00A1DB")); // Blue
        colorList.add(Color.web("98689C")); // Purple
    }


    public void colorizeSunburst(WeightedTreeItem<?> rootItem){
        if(rootItem.getParent() != null){
            throw new IllegalArgumentException("Error: Argument must be root item! Current: " + rootItem.toString());
        }

        for(WeightedTreeItem child : rootItem.getChildrenWeighted()){
            Color color = getSectorColor();
            colorMap.put(child, color);
            ArrayList<Color> colorShades = ColorHelper.getColorShades(color, 20, true);
            colorShadeMap.put(color, colorShades);
            colorizeSunburstRecursive(child, 1, color);
        }
    }

    private void colorizeSunburstRecursive(WeightedTreeItem<?> item, int level, Color color){
        for(WeightedTreeItem<?> child : item.getChildrenWeighted()){
            Color colorShade = colorShadeMap.get(color).get(level);
            colorMap.put(child, colorShade);
            if(!child.isLeaf()){
                colorizeSunburstRecursive(child, ++level, color);
            }
        }

    }

    @Override
    public Color getColor(WeightedTreeItem<?> item) {
        return colorMap.get(item);
    }

    private Color getSectorColor(){
        Random r = new Random();

        Color color;
        if(!colorList.isEmpty()){
            color = colorList.get(r.nextInt(colorList.size()));
            colorList.remove(color);
        } else{
            color = ColorHelper.generateRandomColor();
        }

        return color;
    }
}
