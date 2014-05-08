package controls.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import controls.sunburst.*;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.*;

/**
 * Skin implementation for {@link SunburstView} Control.
 * The primary responsibility is to create all Nodes (DonutUnit) and
 * layout them in the scene-graph.
 */
public class SunburstViewSkin<T> extends BehaviorSkinBase<SunburstView<T>, BehaviorBase<SunburstView<T>>> {

    private final Pane layout = new Pane();
    private final Map<WeightedTreeItem<T>, SunburstSector<T>> sectorMap = new HashMap<>();
    private final Map<WeightedTreeItem<T>, SunburstDonutUnit> donutCache = new HashMap<>();

    private Circle centerCircle;

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

        control.rootItemProperty().addListener(x -> updateRootModel());
        control.selectedItemProperty().addListener(x -> updateSelectedItem());
        control.colorStrategy().addListener(x -> updateRootModel());

        getChildren().clear();
        getChildren().addAll(layout);

        // Most inner circle which on click triggers the zoom out navigation.
        centerCircle = new Circle();
        // Set id in order to apply CSS styles.
        centerCircle.setId("centerCircle");
        centerCircle.setOnMouseClicked(event -> {
            // Check if the root item is reached. If so, further zooming out is impossible.
            if(!getSkinnable().getSelectedItem().equals(getSkinnable().getRootItem())){
                getSkinnable().setSelectedItem((WeightedTreeItem)getSkinnable().getSelectedItem().getParent());
            } else{
                System.out.println("Error: Can't zoom out; Root item reached.");
            }
        });

        updateRootModel();
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

        // Layout most inner circle.
        centerCircle.setCenterX(centerX);
        centerCircle.setCenterY(centerY);
        centerCircle.setRadius(startRadius);

        for(WeightedTreeItem<T> innerChild : currentRoot.getChildrenWeighted()){
            SunburstDonutUnit unit = findView(innerChild);
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

        SunburstDonutUnit parentUnit = findView(parentItem);
        double startDegree = parentUnit.getDegreeStart();
        double fullDegree = parentUnit.getArcAngle();

        for(WeightedTreeItem<T> child : parentItem.getChildrenWeighted()){

            SunburstDonutUnit unit = findView(child);

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
     * We have to ensure that the current selectedItem matches the new root-model.
     */
    private void updateRootModel(){

        final SunburstView<T> control = getSkinnable();
        WeightedTreeItem<T> rootItem =  control.getRootItem();

        clearAll();

        checkSelectedItem();

        // Create sectors
        int sectorNum = 0;
        for(WeightedTreeItem<T> sectorItem : rootItem.getChildrenWeighted()) {
            // Each sector has its own primary color.
            //Color sectorColor = colorStrategy.getColor(sectorItem);
            SunburstSector<T> sector = new SunburstSector<>(sectorItem, sectorNum);
            sectorMap.put(sectorItem, sector);
            sectorNum++;
        }

        updateSelectedItem();
    }

    /**
     * The selected item defines the whole view, thus we update the whole
     * view and create the nodes necessary.
     *
     */
    private void updateSelectedItem(){

        clearCurrentView();

        WeightedTreeItem<T> rootItem = getSkinnable().getRootItem();
        WeightedTreeItem<T> selectedItemRoot = getSkinnable().getSelectedItem();

        if(selectedItemRoot != null && selectedItemRoot != null) {

            layout.getChildren().add(centerCircle);

            if(rootItem.equals(selectedItemRoot)){
                // Root item
                for (WeightedTreeItem<T> child : selectedItemRoot.getChildrenWeighted()) {
                    SunburstSector<T> sector = findSector(child);
                    SunburstDonutUnit unit = findOrCreateView(child);
                    addUnitView(unit, sector.getIndex(), 0);
                    buildUnitsRecursive(child, sector.getIndex(), 1);
                }
            }else{
                // Not root, all have same sector
                SunburstSector<T> sector = findSector(selectedItemRoot);
                for (WeightedTreeItem<T> child : selectedItemRoot.getChildrenWeighted()) {
                    SunburstDonutUnit unit = findOrCreateView(child);
                    addUnitView(unit, sector.getIndex(), 0);
                    buildUnitsRecursive(child, sector.getIndex(), 1);
                }
            }
        }
    }

    private void addUnitView(SunburstDonutUnit unit, int sector, int level){
        IColorStrategy colorStrategy = getSkinnable().getColorStrategy();
        Color color = colorStrategy.colorFor(unit.getItem(), sector, level);
        unit.setFill(color);
        layout.getChildren().add(unit);
    }

    /**
     * Builds the DonutUnits recursively (creates the Nodes) and sets the color
     *
     * @param parentItem
     * @param sector
     * @param level
     */
    private void buildUnitsRecursive(WeightedTreeItem<T> parentItem, int sector, int level){

        for(WeightedTreeItem<T> child : parentItem.getChildrenWeighted()){
            SunburstDonutUnit unit = findOrCreateView(child);

            addUnitView(unit, sector, level);

            if(!child.isLeaf()){
                buildUnitsRecursive(child, sector, level + 1); // Recourse into
            }
        }
    }

    /**
     * Finds the sector for this item.
     *
     * This is achieved by traversing UP the parent references of this item,
     * until the root item is reached.
     *
     * @param item
     * @return
     */
    private SunburstSector<T> findSector(WeightedTreeItem<T> item){

        WeightedTreeItem<T> root = getSkinnable().getRootItem();
        WeightedTreeItem<T> sector = item;

        while (sector != null){
            if(root.equals(sector.getParent())){
                // We found the sector item
                break;
            }
            // Next level
            sector = (WeightedTreeItem<T>)sector.getParent();
        }

        return sector != null ? sectorMap.get(sector) : null;
    }





    /**
     * Check if the current selected item matches to the current root item.
     * If it is inconsistent, we set the selected item to the current root-item.
     */
    private void checkSelectedItem(){
        WeightedTreeItem<T> rootItem = getSkinnable().getRootItem();
        WeightedTreeItem<T> selectedItemRoot = getSkinnable().getSelectedItem();
        if(rootItem != null){
            WeightedTreeItem<T> parent = getRoot(selectedItemRoot);
            if(parent != null && !rootItem.equals(parent)){
                // Inconsistent root-model to selected item data model
                // we need to fix it by clearing the selection
                getSkinnable().setSelectedItem(rootItem);
            }
        }
    }

    /**
     * Clears the complete layout (all nodes)
     * This is called when a the root model has changed
     */
    private void clearAll(){
        sectorMap.clear();
        donutCache.clear();
        clearCurrentView();
    }

    /**
     * Clears the current visible node from the layout.
     * This is called when the selected node has changed
     */
    private void clearCurrentView(){
        layout.getChildren().clear();
    }



    private SunburstDonutUnit findOrCreateView(WeightedTreeItem<T> item){
        SunburstDonutUnit view = findView(item);

        // If we could not find a view for this item, we create one.
        if(view == null){
            view = buildDonutUnit(item);
            donutCache.put(item, view);
        }

        return view;
    }

    private SunburstDonutUnit findView(WeightedTreeItem<T> item){
        return donutCache.get(item);
    }

    /**
     * Build a single DonutUnit which is the smallest Element of this control
     * @param item
     * @return
     */
    private final SunburstDonutUnit buildDonutUnit(WeightedTreeItem<T> item){
        SunburstDonutUnit unit = new SunburstDonutUnit(item);
        unit.setStroke(Color.WHITE);
        return unit;
    }

    /**
     * Traverses the item hierarchy to find the top most root item.
     * @param item
     * @return
     */
    private WeightedTreeItem<T> getRoot(WeightedTreeItem<T> item){
        WeightedTreeItem<T> current = item;
        if(item != null) {
            while (current.getParent() != null) {
                current = (WeightedTreeItem<T>) current.getParent();
            }
        }
        return current;
    }



    /***************************************************************************
     *                                                                         *
     * Internal classes                                                        *
     *                                                                         *
     **************************************************************************/

    /**
     * Represents the smallest interactive component of this control,
     * a single Donut-Unit of this SunburstView.
     */
    private class SunburstDonutUnit extends DonutUnit {

        private final WeightedTreeItem<T> item;

        public SunburstDonutUnit(WeightedTreeItem<T> item){
            this.item = item;
            Tooltip t = new Tooltip(item.getValue().toString());
            Tooltip.install(this, t);

            setOnMouseClicked(event -> {
                // Check if leaf node was clicked. If so there are no more children to display.
                if(!item.getChildren().isEmpty()){
                    getSkinnable().setSelectedItem(item);
                } else{
                    System.out.println("Error: Can't zoom in; There are no children for this DonutUnit");
                }
            });
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
        private final int index;

        public SunburstSector(WeightedTreeItem<T> unit, int index){
            this.item = unit;
            this.index = index;
        }

        public int getIndex() {
            return index;
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
