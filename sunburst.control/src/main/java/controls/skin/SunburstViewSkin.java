package controls.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import controls.sunburst.DonutUnit;
import controls.sunburst.SunburstView;
import controls.sunburst.WeightedTreeItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * Skin implementation for {@link SunburstView} Control.
 */
public class SunburstViewSkin<T> extends BehaviorSkinBase<SunburstView<T>, BehaviorBase<SunburstView<T>>> {

    private final Pane layout = new Pane();

    public SunburstViewSkin(final SunburstView<T> control) {
        super(control, new BehaviorBase<>(control, Collections.<KeyBinding> emptyList()));

        control.rootItemProperty().addListener(x -> updateView());

        getChildren().clear();
        getChildren().addAll(layout);

        updateView();
    }

    private final List<SunburstSector<T>> sectors = new ArrayList<>();

    private void updateView(){

        final SunburstView<T> control = getSkinnable();
        WeightedTreeItem<T> rootItem =  control.getRootItem();

        // Clear existing layout
        layout.getChildren().clear();
        sectors.clear();

        // Create Sectors
        int sectorNum = 0;

        for(WeightedTreeItem<T> sectorItem : rootItem.getChildrenWeighted()) {
            SunburstDonutUnit<T> unit = buildDonutUnit(sectorItem);
            SunburstSector<T> sector = new SunburstSector<>(unit, sectorColor(sectorNum));
            sectors.add(sector);
            updateSector(unit, sector.getColor());
            sectorNum++;
        }
    }

    private void updateSector(SunburstDonutUnit<T> parent, Color color){
        for(WeightedTreeItem<T> child : parent.getItem().getChildrenWeighted()){
            SunburstDonutUnit<T> unit = buildDonutUnit(child);
            unit.setFill(color);

            parent.getChildren().add(unit);

            if(!child.isLeaf()){
                updateSector(unit, color); // Recurse into
            }
        }
    }

    private SunburstDonutUnit<T> buildDonutUnit(WeightedTreeItem<T> item){
        SunburstDonutUnit<T> unit = new SunburstDonutUnit(item);
        layout.getChildren().add(unit);
        return unit;
    }

    private Color sectorColor(int sector){
        Color[] colors = { Color.LIGHTGREEN, Color.BEIGE,  Color.CORNFLOWERBLUE, Color.CRIMSON, Color.GREY};
        return colors[sector % colors.length];
    }



    @Override protected void layoutChildren(double x, double y, double w, double h) {

        double centerX = w/2d;
        double centerY = h/2d;

        double sectorStartDegree = 0;
        for(SunburstSector<T> sector : sectors){
            // For each sector
            SunburstDonutUnit<T> unit = sector.getSectorUnit();

            double sectorAngle = 360d * unit.getItem().getRelativeWeight();

            unit.setDegreeStart(sectorStartDegree);
            unit.setDegreeEnd(sectorStartDegree + sectorAngle);
            unit.setCenterX(centerX);
            unit.setCenterY(centerY);
            unit.setRingWidth(donutWidth);
            unit.setInnerRadius(startRadius);

            layoutChildrenRecursive(unit, centerX, centerY, 1);

            sectorStartDegree += sectorAngle;
        }
    }

    private double donutWidth = 30;
    private double startRadius = 80;
    private double sectorOffset = donutWidth;

    private void layoutChildrenRecursive(SunburstDonutUnit<T> parentUnit, double centerX, double centerY, int level){

        double startDegree = parentUnit.getDegreeStart();
        double fullDegree = parentUnit.getArcAngle();

        for(SunburstDonutUnit<T> unit : parentUnit.getChildren()){

            double angle = fullDegree * unit.getItem().getRelativeWeight();

            unit.setDegreeStart(startDegree);
            unit.setDegreeEnd(startDegree + angle);

            startDegree += angle;

            unit.setCenterX(centerX);
            unit.setCenterY(centerY);
            unit.setRingWidth(donutWidth);
            unit.setInnerRadius(startRadius + (((double)level) * sectorOffset));

            unit.refresh();

            layoutChildrenRecursive(unit, centerX, centerY, level + 1);
        }
    }


    /***************************************************************************
     *                                                                         *
     * Internal classes                                                        *
     *                                                                         *
     **************************************************************************/

    /**
     * Represents the smallest interactive component of this control,
     * a single Donut-Unit of this SunburstView.
     * @param <T>
     */
    private class SunburstDonutUnit<T> extends DonutUnit {

        private final WeightedTreeItem<T> item;

        private final List<SunburstDonutUnit<T>> children = new ArrayList<>();

        public SunburstDonutUnit(WeightedTreeItem<T> item){
            this.item = item;
            Tooltip t = new Tooltip(item.getValue().toString());
            Tooltip.install(this, t);
        }

        public List<SunburstDonutUnit<T>> getChildren(){return children;}

        public WeightedTreeItem<T> getItem(){
            return item;
        }

        public String toString(){
            return "[" + getItem() + "; " + getArcAngle() + "°]";
        }
    }

    /**
     * Represents a Sunburst Sector (refer to a circle sector)
     * @param <T>
     */
    private class SunburstSector<T> {

        private final SunburstDonutUnit<T> unit;
        private final Color color;

        public SunburstSector(SunburstDonutUnit<T> unit, Color color){
            this.unit = unit;
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        /**
         * Gets the inner most sector unit
         * @return
         */
        public SunburstDonutUnit<T> getSectorUnit() {
            return unit;
        }
    }

}
