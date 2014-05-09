package controls.sunburst;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Eric on 09.05.2014.
 */
public class LegendItem extends HBox{

    private Rectangle rect;
    private Label label;


    public LegendItem(Color color, String text) {
        this.label = new Label(text);


        this.rect = new Rectangle();
        this.rect.setHeight(5);
        this.rect.setWidth(10);
        this.rect.setFill(color);


        getChildren().add(rect);
        getChildren().add(label);
    }


    // Getters and Setters
    public Rectangle getRect() {
        return rect;
    }

    public void setRectColor(Color color) {
        this.rect.setFill(color);
    }

    public Label getLabel() {
        return label;
    }

    public void setLabelText(String text) {
        this.label.setText(text);
    }


}
