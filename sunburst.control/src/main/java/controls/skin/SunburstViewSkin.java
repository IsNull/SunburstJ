package controls.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import controls.sunburst.SunburstView;

import java.util.Collections;

/**
 * Skin implementation for {@link SunburstView} Control.
 */
public class SunburstViewSkin extends BehaviorSkinBase<SunburstView, BehaviorBase<SunburstView>> {

    public SunburstViewSkin(final SunburstView control) {
        super(control, new BehaviorBase<>(control, Collections.<KeyBinding> emptyList()));

    }

}
