package controls.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import controls.sunburst.*;
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
    private IColorStrategy colorStrategy;


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
        colorStrategy = new ColorStrategyRandom();
        control.rootItemProperty().addListener(x -> updateRootModel());
        control.selectedItemProperty().addListener(x -> updateSelectedItem());

        getChildren().clear();
        getChildren().addAll(layout);

        updateRootModel();
        updateSelectedItem();
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

        WeightedTreeItem<T> currentRoot = getSkinnable().getSelectedItem();

        for(WeightedTreeItem<T> innerChild : currentRoot.getChildrenWeighted()){
            SunburstDonutUnit<T> unit = findView(innerChild);
            double sectorAngle = 360d * innerChild.getRelativeWeight();

            unit.setCenterX(centerX);
            unit.setCenterY(centerY);

            unit.setDegreeStart(sectorStartDegree);
            unit.setDegreeEnd(sectorStartDegree + sectorAngle);

            unit.setRingWidth(donutWidth);
            unit.setInnerRadius(startRadius);

            unit.refresh();

            layoutChildrenRecursive(innerChild, centerX, centerY, 1);

            sectorStartDegree += sectorAngle;
        }
    }


    /**
     * Layout the given Donut-Units children.
     * The parentUnit's layout must already be setup correctly.
     *
     * @param parentItem The unit of which children should be layout.
     * @param centerX
     * @param centerY
     * @param level
     */
    private void layoutChildrenRecursive(WeightedTreeItem<T> parentItem, double centerX, double centerY, int level){

        SunburstDonutUnit<T> parentUnit = findView(parentItem);
        double startDegree = parentUnit.getDegreeStart();
        double fullDegree = parentUnit.getArcAngle();

        for(WeightedTreeItem<T> child : parentItem.getChildrenWeighted()){

            SunburstDonutUnit<T> unit = findView(child);

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
            layoutChildrenRecursive(child, centerX, centerY, level + 1);
        }
    }

    /***************************************************************************
     *                                                                         *
     * Private implementation                                                  *
     *                                                                         *
     **************************************************************************/

    /**
     * The root model update is responsible for creating the initial sectors and their
     * colors.
     *
     */
    private void updateRootModel(){

        final SunburstView<T> control = getSkinnable();
        WeightedTreeItem<T> rootItem =  control.getRootItem();

        clearLayout();

        // Create sectors
        int sectorNum = 0;
        for(WeightedTreeItem<T> sectorItem : rootItem.getChildrenWeighted()) {
            // Each sector has its own primary color.
            Color sectorColor = colorStrategy.getColor();
            SunburstSector<T> sector = new SunburstSector<>(sectorItem, sectorColor);
            sectors.add(sector);
            sectorNum++;
        }
    }

    /**
     * The selected item defines the whole view, thus we update the whole
     * view and create the nodes necessary.
     *
     */
    private void updateSelectedItem(){

        clearLayout();

        WeightedTreeItem<T> selectedItemRoot = getSkinnable().getSelectedItem();

        if(selectedItemRoot != null) {

            // TODO find sector / sectors for this item root?

            for (WeightedTreeItem<T> child : selectedItemRoot.getChildrenWeighted()) {
                buildUnitsRecursive(child, Color.CORNFLOWERBLUE); // FIXME Strategy & sector?
            }
        }
    }



    /**
     * Builds the DonutUnits recursively (creates the Nodes) and sets the color
     *
     * @param parentItem Parent Item
     * @param color Primary color tone
     */
    private void buildUnitsRecursive(WeightedTreeItem<T> parentItem, Color color){
        SunburstDonutUnit<T> parent = findOrCreateView(parentItem);

        parent.setFill(color);
        for(WeightedTreeItem<T> child : parentItem.getChildrenWeighted()){
            SunburstDonutUnit<T> unit = findOrCreateView(child);
            unit.setFill(color);

            if(!child.isLeaf()){
                buildUnitsRecursive(child, color); // Recourse into
            }
        }
    }


    /**
     * Clears the complete layout (all nodes)
     */
    private void clearLayout(){
        layout.getChildren().clear();
        sectors.clear();
        donutCache.clear();
    }


    private final Map<WeightedTreeItem<T>, SunburstDonutUnit<T>> donutCache = new HashMap<>();

    private SunburstDonutUnit<T> findOrCreateView(WeightedTreeItem<T> item){
        SunburstDonutUnit<T> view = findView(item);

        // If we could not find a view for this item, we create one.
        if(view == null){
            view = buildDonutUnit2(item);
            donutCache.put(item, view);
        }

        return view;
    }

    private SunburstDonutUnit<T> findView(WeightedTreeItem<T> item){
        return donutCache.get(item);
    }

    /**
     * Build a single DonutUnit which is the smallest Element of this control
     * @param item
     * @return
     */
    private final SunburstDonutUnit<T> buildDonutUnit2(WeightedTreeItem<T> item){
        SunburstDonutUnit<T> unit = new SunburstDonutUnit(item);
        unit.setStroke(Color.WHITE);
        layout.getChildren().add(unit); // TODO when add and remove donuts from view?
        return unit;
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

        public SunburstDonutUnit(WeightedTreeItem<T> item){
            this.item = item;
            Tooltip t = new Tooltip(item.getValue().toString());
            Tooltip.install(this, t);
        }

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

        private final WeightedTreeItem<T> item;
        private final Color color;

        public SunburstSector(WeightedTreeItem<T> unit, Color color){
            this.item = unit;
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        /**
         * Gets the inner most sector unit
         * @return
         */
        public WeightedTreeItem<T> getSectorItem() {
            return item;
        }
    }

}
