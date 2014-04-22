package controls.sunburst;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;

/**
 * A Tree Item which is weighted
 */
public class WeightedTreeItem<T> extends TreeItem<T> {

    private final ObjectProperty<Double> weight = new SimpleObjectProperty<>(this, "weight", 0d);

    public WeightedTreeItem(){
        this(0, null);
    }

    public WeightedTreeItem(double weightValue, T value){
        setWeight(weightValue); setValue(value);
    }

    public ObjectProperty<Double> weightProperty(){
        return weight;
    }

    public final double getWeight(){
        return weightProperty().get();
    }

    public void setWeight(double weightValue){
        weightProperty().set(weightValue);
    }
}
