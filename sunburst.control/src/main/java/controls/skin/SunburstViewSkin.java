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

        int sectorNum = 0;
        for(WeightedTreeItem<T> sectorItem : rootItem.getChildrenWeighted()) {
            SunburstDonutUnit<T> innerSectorUnit = buildDonutUnit(sectorItem);

            SunburstSector<T> sector = new SunburstSector<>(innerSectorUnit, sectorColor(sectorNum));
           // innerSectorUnit.setFill(sector.getColor());

            sectors.add(sector);

            updateSector(sector, sectorItem, 1);
            sectorNum++;
        }
    }

    private void updateSector(SunburstSector<T> sector, WeightedTreeItem<T> levelRoot, int level){
        for(WeightedTreeItem<T> child : levelRoot.getChildrenWeighted()){
            SunburstDonutUnit<T> unit = buildDonutUnit(child);
            //unit.setFill(sector.getColor());
            sector.addUntiToLevel(level, unit);
            if(!child.isLeaf()){
                updateSector(sector, child, level + 1);
            }
        }
    }

    private SunburstDonutUnit<T> buildDonutUnit(WeightedTreeItem<T> item){
        SunburstDonutUnit<T> unit = new SunburstDonutUnit(item);
        layout.getChildren().add(unit);
        return unit;
    }

    private Color sectorColor(int sector){
        Color[] colors = { Color.ALICEBLUE, Color.BEIGE, Color.CRIMSON, Color.LIGHTGREEN, Color.CORNFLOWERBLUE };
        return colors[sector % colors.length];
    }


    @Override protected void layoutChildren(double x, double y, double w, double h) {

        double centerX = w/2d;
        double centerY = h/2d;

        double donutWidth = 30;
        double startRadius = 80;
        double sectorOffset = donutWidth;

        double sectorStartAngle = 0;
        for(SunburstSector<T> sector : sectors){
            // For each sector

            double sectorFullAngle = sector.getSectorUnit().getArcAngle();
            System.out.println("sectorFullAngle: " + sectorFullAngle);

            for (int level = 0; level < sector.levelCount(); level++) {
                List<SunburstDonutUnit<T>> currentLevel = sector.getSectorLevel(level);
                double startAngle = sectorStartAngle;

                //System.out.println("Sector " + " Level: " + 0 + " startAngle: " + startAngle);

                for (SunburstDonutUnit<T> donut : currentLevel){

                    donut.setCenterX(centerX);
                    donut.setCenterY(centerY);
                    donut.setRingWidth(donutWidth);
                    donut.setInnerRadius(startRadius + (((double)level) * sectorOffset));

                    System.out.println("startAngle: " + startAngle);
                    donut.setDegreeStart(startAngle);
                    double partAngle = level == 0
                            ? sectorFullAngle
                            : sectorFullAngle * donut.getItem().getRelativeWeight();

                    startAngle += partAngle;
                    donut.setDegreeEnd(startAngle);

                    donut.refresh();
                    System.out.println("Donut: " + donut.toString());
                }
            }

            sectorStartAngle += 360d * sector.getSectorUnit().getItem().getRelativeWeight();
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

        public SunburstDonutUnit(WeightedTreeItem<T> item){
            this.item = item;
            Tooltip t = new Tooltip(item.getValue().toString());
            Tooltip.install(this, t);
        }


        public WeightedTreeItem<T> getItem(){
            return item;
        }

    }

    /**
     * Represents a Sunburst Sector (refer to a circle sector)
     * @param <T>
     */
    private class SunburstSector<T> {
        private final Color color;
        private final SunburstDonutUnit<T> sectorUnit;
        private final Map<Integer, List<SunburstDonutUnit<T>>> sectorLevels = new HashMap<>();

        public SunburstSector(SunburstDonutUnit<T> sectorUnit, Color color){
            this.sectorUnit = sectorUnit;
            this.color = color;
            addUntiToLevel(0, sectorUnit);
        }

        public SunburstDonutUnit<T> getSectorUnit() {
            return sectorUnit;
        }

        public int levelCount(){
            return sectorLevels.size();
        }

        public void addUntiToLevel(int level, SunburstDonutUnit<T> unit){
            List<SunburstDonutUnit<T>> sectorLevel = findOrCreateSectorLevel(level);
            unit.setFill(this.getColor());
            sectorLevel.add(unit);
        }

        public List<SunburstDonutUnit<T>> getSectorLevel(int level){
            return sectorLevels.get(level);
        }


        private List<SunburstDonutUnit<T>> findOrCreateSectorLevel(int level){
            List<SunburstDonutUnit<T>> sectorLevel = sectorLevels.get(level);
            if(sectorLevel == null){
                sectorLevel = new ArrayList<>();
                sectorLevels.put(level, sectorLevel);
            }
            return sectorLevel;
        }

        public Color getColor() {
            return color;
        }
    }

}
