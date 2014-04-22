package controls.sunburst;

import controls.skin.SunburstViewSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * A Sunburst View visualizes weighted hierarchical data structures.
 * Refer to the data model for more information.
 *
 */
public class SunburstView<T> extends Control {

    /***************************************************************************
     *                                                                         *
     * Private fields                                                          *
     *                                                                         *
     **************************************************************************/

    private final ObjectProperty<WeightedTreeItem<T>> rootItem = new SimpleObjectProperty<>(this, "rootItem");


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
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Sets the root node of this view
     * @param root
     */
    public void setRootItem(WeightedTreeItem<T> root){
        rootItem.set(root);
    }

    public WeightedTreeItem<T> getRootItem(){
        return rootItem.get();
    }



    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public ObjectProperty<WeightedTreeItem<T>> rootItemProperty(){
        return rootItem;
    }

    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "sunburst-view"; //$NON-NLS-1$

    /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new SunburstViewSkin<T>(this);
    }

    /** {@inheritDoc} */
    @Override protected String getUserAgentStylesheet() {
        return SunburstView.class.getResource("sunburstview.css").toExternalForm(); //$NON-NLS-1$
    }
}