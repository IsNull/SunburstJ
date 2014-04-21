package controls.sunburst;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * Represents a semi ring (A part of a Donut)
 */
public class DonutUnit extends Path {

    public DonutUnit(){

        drawDonutUnit(
                200,      // Center X
                200,      // Center Y
                -90,    // Start angle
                90,     // End angle
                40,     // Inner Radius
                30);   // Ring-Width


        this.setFill(Color.LIGHTGREEN);
        this.setStroke(Color.DARKGREEN);
        this.setFillRule(FillRule.EVEN_ODD);
    }


    /**
     * Creates a semi ring (Donut) path
     *
     * @param centerX Center Point X Coordinate
     * @param centerY Center Point Y Coordinate
     * @param degreeStart Start angle in degree, may be negative.
     * @param degreeEnd End angle in degree, may be negative.
     * @param innerRadius Inner radius of the Donut
     * @param ringWidth Width of the Donut ring.
     */
    private void drawDonutUnit(double centerX, double centerY, double degreeStart, double degreeEnd, double innerRadius, double ringWidth) {
        double angleAlpha = degreeStart * (Math.PI / 180);
        double angleAlphaNext = degreeEnd * (Math.PI / 180);

        double outerRadius = innerRadius + ringWidth;

        //Point 1
        double pointX1 = centerX + innerRadius * Math.sin(angleAlpha);
        double pointY1 = centerY - innerRadius * Math.cos(angleAlpha);

        //Point 2
        double pointX2 = centerX + outerRadius * Math.sin(angleAlpha);
        double pointY2 = centerY - outerRadius * Math.cos(angleAlpha);

        //Point 3
        double pointX3 = centerX + outerRadius * Math.sin(angleAlphaNext);
        double pointY3 = centerY - outerRadius * Math.cos(angleAlphaNext);

        //Point 4
        double pointX4 = centerX + innerRadius * Math.sin(angleAlphaNext);
        double pointY4 = centerY - innerRadius * Math.cos(angleAlphaNext);


        this.getElements().add(new MoveTo(pointX1, pointY1)); // Move to Point 1
        this.getElements().add(new LineTo(pointX2, pointY2)); // Draw a Line to Point 2

        ArcTo arcOuter = new ArcTo();                         // Draw an Arc to Point 3
        arcOuter.setRadiusX(outerRadius);
        arcOuter.setRadiusY(outerRadius);
        arcOuter.setX(pointX3);
        arcOuter.setY(pointY3);
        arcOuter.setSweepFlag(true);
        this.getElements().add(arcOuter);

        this.getElements().add(new LineTo(pointX4, pointY4)); // Draw a Line to Point 4

        ArcTo arcInner = new ArcTo();                         // Draw an Arc to Point 1
        arcInner.setRadiusX(innerRadius);
        arcInner.setRadiusY(innerRadius);
        arcInner.setX(pointX1);
        arcInner.setY(pointY1);
        arcInner.setSweepFlag(false);
        this.getElements().add(arcInner);
    }

}
