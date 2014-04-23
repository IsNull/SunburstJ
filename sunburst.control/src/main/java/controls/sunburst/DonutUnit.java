package controls.sunburst;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * Represents a semi ring (A part of a Donut)
 */
public class DonutUnit extends Path {

    /***************************************************************************
     *                                                                         *
     * Private final fields                                                    *
     *                                                                         *
     **************************************************************************/

    private final MoveTo moveToPoint1 = new MoveTo();
    private final LineTo lineToPoint2 = new LineTo();
    private final ArcTo arcOuter = new ArcTo();
    private final LineTo lineToPoint4 = new LineTo();
    private final ArcTo arcInner = new ArcTo();

    /***************************************************************************
     *                                                                         *
     * Private fields                                                          *
     *                                                                         *
     **************************************************************************/

    private double centerX = 0;
    private double centerY = 0;

    private double degreeStart = 90;
    private double degreeEnd = 300;
    private double innerRadius = 80;
    private double ringWidth = 30;


    /***************************************************************************
     *                                                                         *
     * Constructor                                                             *
     *                                                                         *
     **************************************************************************/

    public DonutUnit() {
        this.getElements().add(moveToPoint1); // Move to Point 1
        this.getElements().add(lineToPoint2); // Draw a Line to Point 2

        arcOuter.setSweepFlag(true);
        this.getElements().add(arcOuter); // Draw an Arc to Point 3

        this.getElements().add(lineToPoint4); // Draw a Line to Point 4

        arcInner.setSweepFlag(false);
        this.getElements().add(arcInner);


        this.setFill(Color.LIGHTGREEN);
        this.setStroke(Color.DARKGREEN);
        this.setFillRule(FillRule.EVEN_ODD);


        updateDonutUnit();
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    public double getCenterX(){
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public double getDegreeStart() {
        return degreeStart;
    }

    public void setDegreeStart(double degreeStart) {
        if(checkAngle(degreeStart, getDegreeEnd()))
            this.degreeStart = degreeStart;
        else {
            throw new IllegalArgumentException("Error setting DegreeStart: ArcAngle would be bigger than 360°, current ArcAngle: " + getArcAngle() + " given DegreeStart: " + degreeStart);
        }
    }

    public double getDegreeEnd() {
        return degreeEnd;
    }

    public void setDegreeEnd(double degreeEnd) {
        if(checkAngle(getDegreeStart(), degreeEnd))
            this.degreeEnd = degreeEnd;
        else {
            throw new IllegalArgumentException("Error setting DegreeEnd: ArcAngle would be bigger than 360°, current ArcAngle: " + getArcAngle() + " given DegreeEnd: " + degreeEnd);
        }
    }

    public double getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(double innerRadius) {
        this.innerRadius = innerRadius;
    }

    public double getRingWidth() {
        return ringWidth;
    }

    public void setRingWidth(double ringWidth) {
        this.ringWidth = ringWidth;
    }

    /***************************************************************************
     *                                                                         *
     * Read-Only Properties                                                    *
     *                                                                         *
     **************************************************************************/


    /**
     * Returns the current ArcAngle.
     * @return
     */
    public double getArcAngle() {
        return calculateAngle(getDegreeStart(), getDegreeEnd());
    }



    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/


    /**
     * Updates the Donut shape
     */
    public void refresh(){
        updateDonutUnit();
    }

    @Override
    public String toString(){
        return "{start: "+ getDegreeStart()+"; end: " + getDegreeEnd() + "; arc angle: " + getArcAngle() + "}";
    }

    /***************************************************************************
     *                                                                         *
     * Private methods                                                         *
     *                                                                         *
     **************************************************************************/

    /**
     * Updates the Donut shape
     *
     *  centerX: Center Point X Coordinate
     *  centerY: Center Point Y Coordinate
     *  degreeStart: Start angle in degree, may be negative.
     *  degreeEnd: End angle in degree, may be negative.
     *  innerRadius: Inner radius of the Donut
     *  ringWidth: Width of the Donut ring.
     */
    private void updateDonutUnit() {
        double angleAlpha = degreeStart * (Math.PI / 180);
        double angleAlphaNext = degreeEnd * (Math.PI / 180);

        double outerRadius = innerRadius + ringWidth;

        //Point 1, inner end point
        double pointX1 = centerX + innerRadius * Math.sin(angleAlpha);
        double pointY1 = centerY - innerRadius * Math.cos(angleAlpha);

        //Point 2, outer end point
        double pointX2 = centerX + outerRadius * Math.sin(angleAlpha);
        double pointY2 = centerY - outerRadius * Math.cos(angleAlpha);

        //Point 3, outer start point
        double pointX3 = centerX + outerRadius * Math.sin(angleAlphaNext);
        double pointY3 = centerY - outerRadius * Math.cos(angleAlphaNext);

        //Point 4, inner start point
        double pointX4 = centerX + innerRadius * Math.sin(angleAlphaNext);
        double pointY4 = centerY - innerRadius * Math.cos(angleAlphaNext);


        boolean isLargeArc = getArcAngle() > 180;

        moveToPoint1.setX(pointX1); // Move to Point 1
        moveToPoint1.setY(pointY1);

        lineToPoint2.setX(pointX2); // Draw a Line to Point 2
        lineToPoint2.setY(pointY2);

        arcOuter.setRadiusX(outerRadius); // Draw an Arc to Point 3
        arcOuter.setRadiusY(outerRadius);
        arcOuter.setX(pointX3);
        arcOuter.setY(pointY3);
        arcOuter.setSweepFlag(true);
        arcOuter.setLargeArcFlag(isLargeArc);

        lineToPoint4.setX(pointX4); // Draw a Line to Point 4
        lineToPoint4.setY(pointY4);

        arcInner.setRadiusX(innerRadius);  // Draw an Arc to Point 1
        arcInner.setRadiusY(innerRadius);
        arcInner.setX(pointX1);
        arcInner.setY(pointY1);
        arcInner.setSweepFlag(false);
        arcInner.setLargeArcFlag(isLargeArc);
    }


    /**
     * Checks if the ArcAngle would be greater than 360° which is not allowed.
     * @param startAngle
     * @param endAngle
     * @return
     */
    private boolean checkAngle(double startAngle, double endAngle){
        return (calculateAngle(startAngle, endAngle) <= 360 ) ? true : false;
    }

    /**
     * Calculates the ArcAngle alpha of the circular sector by the given start and end angle.
     * @param startAngle
     * @param endAngle
     * @return The ArcAngle alpha
     */
    private double calculateAngle(double startAngle, double endAngle){

        if(startAngle < 0){
            throw new IllegalArgumentException("startAngle must be > 0, current: " + startAngle);
        }else if (endAngle < 0){
            throw new IllegalArgumentException("endAngle must be > 0, current: " + endAngle);
        }

        double alpha = 0;

        if(endAngle > startAngle){
            alpha = endAngle - startAngle;
        }
        else if(startAngle > endAngle){
            alpha = 360 - (startAngle - endAngle);
        }

        return alpha;
    }

}
