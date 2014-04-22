package controls.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import controls.sunburst.SunburstView;
import controls.sunburst.WeightedTreeItem;
import javafx.scene.Node;

import java.util.Collections;

/**
 * Skin implementation for {@link SunburstView} Control.
 */
public class SunburstViewSkin<T> extends BehaviorSkinBase<SunburstView<T>, BehaviorBase<SunburstView<T>>> {

    public SunburstViewSkin(final SunburstView<T> control) {
        super(control, new BehaviorBase<>(control, Collections.<KeyBinding> emptyList()));

        control.rootNodeProperty().addListener(x -> updateView());

        updateView();
    }

    private void updateView(){

        final SunburstView<T> control = getSkinnable();
        WeightedTreeItem<T> root =  control.getRootNode();

        // TODO Put the Donut-Units in a pane and layout them correctly

        // For each node (until max nesting reached)


    }

    @Override protected void layoutChildren(double x, double y, double w, double h) {
        for (int i = 0; i < getChildren().size(); i++) {
            Node n = getChildren().get(i);

            double nw = snapSize(n.prefWidth(h));
            double nh = snapSize(n.prefHeight(-1));

            /*
            if (i > 0) {
                // We have to position the bread crumbs slightly overlapping
                double ins = n instanceof BreadCrumbButton ?  ((BreadCrumbButton)n).getArrowWidth() : 0;
                x = snapPosition(x - ins);
            }*/

            n.resize(nw, nh);
            n.relocate(x, y);
            x += nw;
        }
    }

}
