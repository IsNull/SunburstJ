package controls.sunburst;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * Created by IsNull on 17.04.14.
 */
public class DonutUnit extends Path {

    public DonutUnit(){


        drawSemiRing(120, 120, 100, 50);

        this.setFill(Color.LIGHTGREEN);
        this.setStroke(Color.DARKGREEN);
        this.setFillRule(FillRule.EVEN_ODD);

    }

    private void drawDonutUnit(double radius, double width, double angle){

        double innerRadius = radius;
        double outerRadius = radius + width;


        MoveTo moveTo = new MoveTo();
        moveTo.setX(0);
        moveTo.setY(0);
        this.getElements().add(moveTo);

        /*
        ArcTo arcToInner = new ArcTo();
        arcToInner.setX(centerX - innerRadius);
        arcToInner.setY(centerY);
        arcToInner.setRadiusX(innerRadius);
        arcToInner.setRadiusY(innerRadius);
        */

    }


    private void drawSemiRing(double centerX, double centerY, double radius, double innerRadius) {

        MoveTo moveTo = new MoveTo();
        moveTo.setX(centerX + innerRadius);
        moveTo.setY(centerY);

        ArcTo arcToInner = new ArcTo();
        arcToInner.setX(centerX - innerRadius);
        arcToInner.setY(centerY);
        arcToInner.setRadiusX(innerRadius);
        arcToInner.setRadiusY(innerRadius);

        MoveTo moveTo2 = new MoveTo();
        moveTo2.setX(centerX + innerRadius);
        moveTo2.setY(centerY);

        HLineTo hLineToRightLeg = new HLineTo();
        hLineToRightLeg.setX(centerX + radius);

        ArcTo arcTo = new ArcTo();
        arcTo.setX(centerX - radius);
        arcTo.setY(centerY);
        arcTo.setRadiusX(radius);
        arcTo.setRadiusY(radius);

        HLineTo hLineToLeftLeg = new HLineTo();
        hLineToLeftLeg.setX(centerX - innerRadius);

        this.getElements().add(moveTo);
        this.getElements().add(arcToInner);
        this.getElements().add(moveTo2);
        this.getElements().add(hLineToRightLeg);
        this.getElements().add(arcTo);
        this.getElements().add(hLineToLeftLeg);
    }


}
