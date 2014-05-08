package controls.sunburst;

import helpers.ColorHelper;
import javafx.scene.paint.Color;

/**
 * A simple strategy which assigns a random color to each donut-unit.
 */
public class ColorStrategyRandom implements IColorStrategy{

    @Override
    public Color colorFor(WeightedTreeItem<?> item, int sector, int level) {
        return ColorHelper.generateMixedRandomColor(Color.WHITE);
    }
}
