package controls.sunburst;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * A Tree Item which is weighted
 */
public class WeightedTreeItem<T> extends TreeItem<T> {

    private final ObjectProperty<Double> weight = new SimpleObjectProperty<>(this, "weight", 0d);

    public WeightedTreeItem() {
        this(0, null);
    }

    public WeightedTreeItem(double weightValue, T value) {
        setWeight(weightValue);
        setValue(value);
    }

    public ObjectProperty<Double> weightProperty() {
        return weight;
    }

    public final double getWeight() {
        return weightProperty().get();
    }

    public void setWeight(double weightValue) {
        weightProperty().set(weightValue);
    }

    public ObservableList<WeightedTreeItem<T>> getChildrenWeighted() {
        return (ObservableList<WeightedTreeItem<T>>) (ObservableList<?>) getChildren();
    }

    // private final double DIRTY_FLAG = -1;
    // private double totalChildrenAbsoluteWeight = DIRTY_FLAG;


    /**
     * Gets the relative weight of this node among its local level
     * The relative weight is in a range of [0.0 - 1.0]
     *
     * @return
     */
    public double getRelativeWeight() {
        double relativeWeight = 1;
        if (getParent() instanceof WeightedTreeItem<?>) {
            WeightedTreeItem<T> parent = (WeightedTreeItem<T>) getParent();
            relativeWeight = 1 / parent.getChildrenAbsoluteWeight() * getWeight();
        }
        return relativeWeight;
    }

    /**
     * Get the absolute total weight of the children.
     *
     * @return
     */
    public synchronized double getChildrenAbsoluteWeight() {
        double totalChildrenAbsoluteWeight = 0;
        for (WeightedTreeItem<T> child : getChildrenWeighted()) {
            totalChildrenAbsoluteWeight += child.getWeight();
        }
        return totalChildrenAbsoluteWeight;
    }

    public String toString(){
        return "(" + getValue().toString() + " : " + getRelativeWeight()+ ")";
    }

}
