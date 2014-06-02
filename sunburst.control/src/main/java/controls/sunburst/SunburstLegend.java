package controls.sunburst;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the legend visual for a sunburst view.
 *
 */
public class SunburstLegend extends VBox {

    /***************************************************************************
     *                                                                         *
     * Private fields                                                          *
     *                                                                         *
     **************************************************************************/

    private final SunburstView<?> sunburstView;

    private final List<LegendItem> legendItems = new ArrayList<>();

    private int legendItemMax;

    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new SunburstLegend for the given SunburstView
     * @param sunburstView
     */
    public SunburstLegend(SunburstView sunburstView) {
        this.sunburstView = sunburstView;
        this.setAlignment(Pos.CENTER);

        sunburstView.selectedItemProperty().addListener(x -> updateLegend());
        sunburstView.rootItemProperty().addListener(x -> updateLegend());
        sunburstView.legendVisibility().addListener(x -> updateLegend());

        setLegendItemMax(20);
    }



    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    public int getLegendItemMax() {
        return legendItemMax;
    }

    public void setLegendItemMax(int legendItemMax) {
        this.legendItemMax = legendItemMax;
    }

    /**
     * Clears the content of the legend.
     */
    public void clearLegend() {
        this.getChildren().clear();
    }


    /***************************************************************************
     *                                                                         *
     * Private implementation                                                  *
     *                                                                         *
     **************************************************************************/

    /**
     * Updates the legend by setting the color and text values of the inner most units.
     * There will be generated as many LegendItems as needed.
     * This method is called by the updateSelectedItem method.
     */
    private void updateLegend() {

        if (!sunburstView.getLegendVisibility()) {
            clearLegend();
        } else {

            clearLegend();

            WeightedTreeItem<?> currentRoot = sunburstView.getSelectedItem();

            int count = 0;
            for (WeightedTreeItem innerChild : currentRoot.getChildrenWeighted()) {

                if (count < legendItemMax) {

                    String value = (String) innerChild.getValue();

                    Color color = sunburstView.getItemColor(innerChild);
                    System.out.println(color);

                    if (count < legendItems.size()) {

                        LegendItem item = legendItems.get(count);
                        item.setLabelText(value);
                        item.setRectColor(color);
                        this.getChildren().add(item);
                    } else {
                        LegendItem item = new LegendItem(color, value);
                        legendItems.add(item);
                        this.getChildren().add(item);
                    }
                    count++;

                }
            }
        }
    }
}
