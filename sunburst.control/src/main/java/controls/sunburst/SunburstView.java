package controls.sunburst;

import controls.skin.SunburstViewSkin;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * A Sunburst View visualizes weighted hierarchical data structures.
 * Refer to the data model for more information.
 *
 */
public class SunburstView extends Control {


    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new SunburstView
     */
    public SunburstView(){

    }




    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "sunburst-view"; //$NON-NLS-1$

    /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new SunburstViewSkin(this);
    }

    /** {@inheritDoc} */
    @Override protected String getUserAgentStylesheet() {
        return SunburstView.class.getResource("sunburstview.css").toExternalForm(); //$NON-NLS-1$
    }


}
