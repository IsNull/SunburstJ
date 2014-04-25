package controls.sunburst;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Color scheme which returns a random color.
 * Created by n0daft on 25.04.2014.
 */
public class ColorStrategyRandom implements IColorStrategy {
    @Override
    public Color getColor() {

        Random r = new Random();
        int rCol1 = r.nextInt(256);
        int rCol2 = r.nextInt(256);
        int rCol3 = r.nextInt(256);

        return Color.rgb(rCol1, rCol2, rCol3);
    }
}
