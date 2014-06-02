package controls.sunburst;

import controls.skin.SunburstViewSkin;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n0daft on 02.06.2014.
 */
public class SunburstLegend extends VBox {

    /***************************************************************************
     *                                                                         *
     * Private fields                                                          *
     *                                                                         *
     **************************************************************************/

    private SunburstView<?> sunburstView;

    private List<LegendItem> legendItems = new ArrayList<>();

    private int legendItemMax = 20;

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    public SunburstLegend(SunburstView sunburstView){
        this.sunburstView = sunburstView;

        sunburstView.selectedItemProperty().addListener(x -> updateLegend());

        updateLegend();

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
    private void updateLegend(){
            WeightedTreeItem<?> currentRoot = sunburstView.getSelectedItem();

            int count = 0;
            for (WeightedTreeItem innerChild : currentRoot.getChildrenWeighted()) {

                if(count < legendItemMax){

                    String value = (String) innerChild.getValue();
                    Color color = sunburstView.getItemColor(innerChild);

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
