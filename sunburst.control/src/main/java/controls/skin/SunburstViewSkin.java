package controls.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import controls.sunburst.SunburstView;
import controls.sunburst.WeightedTreeItem;

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

}
