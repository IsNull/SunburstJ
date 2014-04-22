package controls.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import controls.sunburst.DonutUnit;
import controls.sunburst.SunburstView;
import controls.sunburst.WeightedTreeItem;
import javafx.scene.layout.Pane;

import java.util.*;

/**
 * Skin implementation for {@link SunburstView} Control.
 */
public class SunburstViewSkin<T> extends BehaviorSkinBase<SunburstView<T>, BehaviorBase<SunburstView<T>>> {

    private final Pane layout = new Pane();

    public SunburstViewSkin(final SunburstView<T> control) {
        super(control, new BehaviorBase<>(control, Collections.<KeyBinding> emptyList()));

        control.rootNodeProperty().addListener(x -> updateView());

        getChildren().clear();
        getChildren().addAll(layout);

        updateView();
    }

    private final List<List<SunburstDonut<T>>> levels = new ArrayList<>();

    private void updateView(){

        final SunburstView<T> control = getSkinnable();
        WeightedTreeItem<T> rootItem =  control.getRootNode();

        // TODO Put the Donut-Units in a pane and layout them correctly
        // For each node (until max nesting reached)

        layout.getChildren().clear();
        levels.clear();

        int level = 0;
        levels.add(level, new ArrayList<>());

        for(WeightedTreeItem<T> segment : rootItem.getChildrenWeighted()) {
            SunburstDonut<T> segmentView = new SunburstDonut(segment);
            layout.getChildren().add(segmentView);
            levels.get(level).add(segmentView);
            // TODO each segment own color

            // TODO nesting segment levels...
        }
    }

    @Override protected void layoutChildren(double x, double y, double w, double h) {



        for (int i = 0; i < levels.size(); i++) {
            List<SunburstDonut<T>> currentLevel = levels.get(i);
            double levelStartAngle = 0;
            int locate = 200;
            for (SunburstDonut<T> donut : currentLevel){

                donut.setCenterX(200);
                donut.setCenterY(200);

                //donut.relocate(0, 0);
               // locate += 100;

                donut.setDegreeStart(levelStartAngle);
                double partAngle = 360d * donut.getItem().getRelativeWeight();
                levelStartAngle += partAngle;
                donut.setDegreeEnd(levelStartAngle);

                donut.refresh();

                System.out.println("Donut: " + donut.toString());
            }
        }

    }


    /***************************************************************************
     *                                                                         *
     * Internal classes                                                        *
     *                                                                         *
     **************************************************************************/

    private class SunburstDonut<T> extends DonutUnit {

        private final WeightedTreeItem<T> item;

        public SunburstDonut(WeightedTreeItem<T> item){
            this.item = item;
        }


        public WeightedTreeItem<T> getItem(){
            return item;
        }


    }

}
