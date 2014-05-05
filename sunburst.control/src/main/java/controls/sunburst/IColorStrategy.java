package controls.sunburst;

import javafx.scene.paint.*;


/**
 * Defines a strategy by which the color scheme of the sunburst view is set.
 * Created by n0daft on 25.04.2014.
 */
public interface IColorStrategy {

   Color getColor(WeightedTreeItem<?> item);

   void colorizeSunburst(WeightedTreeItem<?> rootItem);
}
