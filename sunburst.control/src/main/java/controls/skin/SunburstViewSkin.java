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
 * The primary responsibility is to create all Nodes (DonutUnit) and
 * layout them in the scene-graph.
 */
public class SunburstViewSkin<T> extends BehaviorSkinBase<SunburstView<T>, BehaviorBase<SunburstView<T>>> {

    private final Pane layout = new Pane();
    private final List<SunburstSector<T>> sectors = new ArrayList<>();
    // TODO make this dynamic (random?) or increase list
    private Color[] colors = { Color.LIGHTGREEN, Color.GREY,   Color.CORNFLOWERBLUE, Color.CRIMSON, Color.ORANGE};


    // TODO Make these control / CSS properties
    private double donutWidth = 30;
    private double startRadius = 80;
    private double sectorOffset = donutWidth;


    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new SunburstViewSkin
     * @param control
     */
    public SunburstViewSkin(final SunburstView<T> control) {
        super(control, new BehaviorBase<>(control, Collections.<KeyBinding> emptyList()));

        control.rootItemProperty().addListener(x -> updateView());

        getChildren().clear();
        getChildren().addAll(layout);

        updateView();
    }

    /***************************************************************************
     *                                                                         *
     * Overridden API                                                          *
     *                                                                         *
     **************************************************************************/

    /**
     * Layout all children (all DonutUnits).
     * This method does the layout for all DonutUnits of the inner most Circle.
     * Then it uses {#layoutChildrenRecursive} to layout these root Units children
     * recursively.
     *
     * @param x pos
     * @param y pos
     * @param w width of this control
     * @param h height of this control
     */
    @Override protected void layoutChildren(double x, double y, double w, double h) {

        double centerX = w/2d;
        double centerY = h/2d;

        double sectorStartDegree = 0;

        for(SunburstSector<T> sector : sectors){
            SunburstDonutUnit<T> unit = sector.getSectorUnit();

            double sectorAngle = 360d * unit.getItem().getRelativeWeight();

            unit.setCenterX(centerX);
            unit.setCenterY(centerY);

            unit.setDegreeStart(sectorStartDegree);
            unit.setDegreeEnd(sectorStartDegree + sectorAngle);

            unit.setRingWidth(donutWidth);
            unit.setInnerRadius(startRadius);

            unit.refresh();

            layoutChildrenRecursive(unit, centerX, centerY, 1);

            sectorStartDegree += sectorAngle;
        }
    }

    /**
     * Layout the given Donut-Units children.
     * The parentUnit's layout must already be setup correctly.
     *
     * @param parentUnit The unit of which children should be layout.
     * @param centerX
     * @param centerY
     * @param level
     */
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

            // Now recourse into to layout this child units children, and so forth..
            layoutChildrenRecursive(unit, centerX, centerY, level + 1);
        }
    }

    /***************************************************************************
     *                                                                         *
     * Private implementation                                                  *
     *                                                                         *
     **************************************************************************/

    /**
     * The update is responsible for creating all child nodes,
     * and setting their visual properties (i.e. Color)
     *
     */
    private void updateView(){

        final SunburstView<T> control = getSkinnable();
        WeightedTreeItem<T> rootItem =  control.getRootItem();

        clearLayout();

        // Create sectors
        int sectorNum = 0;
        for(WeightedTreeItem<T> sectorItem : rootItem.getChildrenWeighted()) {
            SunburstDonutUnit<T> unit = buildDonutUnit(sectorItem);

            // Each sector has its own primary color
            Color sectorColor = sectorColor(sectorNum);
            SunburstSector<T> sector = new SunburstSector<>(unit, sectorColor);
            sectors.add(sector);

            buildUnitsRecursive(unit, sector.getColor());
            sectorNum++;
        }
    }

    /**
     * Clears the complete layout (all nodes)
     */
    private void clearLayout(){
        layout.getChildren().clear();
        sectors.clear();
    }

    /**
     * Builds the DonutUnits recursively (creates the Nodes) and sets the color
     *
     * @param parent Parent DonutUnit
     * @param color Primary color tone
     */
    private void buildUnitsRecursive(SunburstDonutUnit<T> parent, Color color){
        parent.setFill(color);
        for(WeightedTreeItem<T> child : parent.getItem().getChildrenWeighted()){
            SunburstDonutUnit<T> unit = buildDonutUnit(child);
            unit.setFill(color);

            parent.getChildren().add(unit);

            if(!child.isLeaf()){
                buildUnitsRecursive(unit, color); // Recourse into
            }
        }
    }

    /**
     * Build a single DonutUnit which is the smallest Element of this control
     * @param item
     * @return
     */
    private SunburstDonutUnit<T> buildDonutUnit(WeightedTreeItem<T> item){
        SunburstDonutUnit<T> unit = new SunburstDonutUnit(item);
        unit.setStroke(Color.WHITE);
        layout.getChildren().add(unit);
        return unit;
    }

    /**
     * Gets the Color for the given sector
     * @param sector Index of the sector
     * @return
     */
    private Color sectorColor(int sector){
       return colors[sector % colors.length];
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
