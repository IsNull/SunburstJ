package controls.sunburst;

import controls.skin.SunburstViewSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;

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

    private final ObjectProperty<WeightedTreeItem<T>> rootItem = new SimpleObjectProperty<>(this, "rootItem", null);

    private final ObjectProperty<WeightedTreeItem<T>> selectedItem = new SimpleObjectProperty<>(this, "selectedItem", null);

    private final ObjectProperty<IColorStrategy> colorStrategy = new SimpleObjectProperty<>(this, "colorStrategy", new ColorStrategySectorShades());

    private final ObjectProperty<Integer> maxDeepness = new SimpleObjectProperty<>(this, "maxDeepness", 8);


    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a new SunburstView
     */
    public SunburstView(){
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * Sets the root node of this view which makes up the whole data model.
     * It will set the selected item to this root item as well.
     * @param root
     */
    public void setRootItem(WeightedTreeItem<T> root){
        setSelectedItem(null);
        rootItem.set(root);
        setSelectedItem(root);
    }

    public WeightedTreeItem<T> getRootItem(){
        return rootItem.get();
    }

    /**
     * Sets the selected item which becomes the center item (parent) of the inner circle.
     * By default, the {link #getRootItem} is set as selected item.
     * @param root
     */
    public void setSelectedItem(WeightedTreeItem<T> root){
        selectedItem.set(root);
    }

    /**
     * Gets the current selected item, which is the item currently in the middle
     * of the Sunburst.
     * @return
     */
    public WeightedTreeItem<T> getSelectedItem(){
        return selectedItem.get();
    }

    /**
     * Gets the current strategy by which the sunburst donut units are colorized.
     * @return
     */
    public IColorStrategy getColorStrategy(){
        return colorStrategy.get();
    }

    /**
     * Sets the strategy by which the sunburst donut units are colorized.
     * @param colorStrategy
     */
    public void setColorStrategy(IColorStrategy colorStrategy) { this.colorStrategy.set(colorStrategy); }

    /**
     * Sets the maximum deepness to which the Sunburst shows the donut units.
     * @param maxDeepnessValue
     */
    public void setMaxDeepness(int maxDeepnessValue){
        maxDeepness.set(maxDeepnessValue);
    }

    /**
     * Returns the maximum deepness.
     * @return
     */
    public int getMaxDeepness(){
        return maxDeepness.get();
    }


    /**
     * Returns the color of the given item.
     * @param item The item for which the color should be returned
     * @return
     */
    public Color getItemColor(WeightedTreeItem<T> item) {

        Skin<?> skin = this.getSkin();
        Color color = null;
        if(skin instanceof SunburstViewSkin){
            System.out.println("is SunburstViewSkin");
           color =((SunburstViewSkin) skin).getItemColor(item);
        }

        return color;

    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public ObjectProperty<WeightedTreeItem<T>> rootItemProperty(){
        return rootItem;
    }

    public ObjectProperty<WeightedTreeItem<T>> selectedItemProperty() {
        return selectedItem;
    }

    public ObjectProperty<IColorStrategy> colorStrategy() {
        return colorStrategy;
    }

    public ObjectProperty<Integer> maxDeepness() {
        return maxDeepness;
    }

    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    private static final String DEFAULT_STYLE_CLASS = "sunburst-view"; //$NON-NLS-1$

    /** {@inheritDoc} */
    @Override protected Skin<?> createDefaultSkin() {
        return new SunburstViewSkin<>(this);
    }

    /** {@inheritDoc} */
    @Override protected String getUserAgentStylesheet() {
        return SunburstView.class.getResource("sunburstview.css").toExternalForm(); //$NON-NLS-1$
    }

}