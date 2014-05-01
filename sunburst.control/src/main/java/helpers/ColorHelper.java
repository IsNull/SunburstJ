package helpers;

import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by Eric on 01.05.2014.
 */
public class ColorHelper {


    public static ArrayList<Color> getColorShades(Color color, int bands, boolean brighten) {

        ArrayList<Color> colorBands = new ArrayList<>(bands);

        if(brighten){
            for (int index = 0; index < bands; index++) {
                colorBands.add(brighten(color, (double) index / (double) bands));
            }
        } else {
            for (int index = 0; index < bands; index++) {
                colorBands.add(darken(color, (double) index / (double) bands));
            }
        }

        return colorBands;

    }

    private static Color brighten(Color color, double fraction) {
        double red =  (Math.min(1d, color.getRed() + 1d * fraction));
        double green = (Math.min(1d, color.getGreen() + 1d * fraction));
        double blue =  (Math.min(1d, color.getBlue() + 1d * fraction));

        double alpha = color.getOpacity();

        return new Color(red, green, blue, alpha);
    }

    private static Color darken(Color color, double fraction) {
        double red =  (Math.max(0d, color.getRed() - 1d * fraction));
        double green = (Math.max(0d, color.getGreen() - 1d * fraction));
        double blue =  (Math.max(0d, color.getBlue() - 1d * fraction));

        double alpha = color.getOpacity();

        return new Color(red, green, blue, alpha);
    }
}
