package controls.sunburst;

import helpers.ColorHelper;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Created by n0daft on 05.05.2014.
 */
public class ColorStrategyRandom implements IColorStrategy{
    @Override
    public Color getColor(WeightedTreeItem item) {
        return ColorHelper.generateMixedRandomColor(Color.WHITE);
    }

    @Override
    public void colorizeSunburst(WeightedTreeItem rootItem) {

    }


}
