package controls.sunburst;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Created by n0daft on 05.05.2014.
 */
public class ColorStrategyRandom implements IColorStrategy{
    @Override
    public Color getColor(WeightedTreeItem item) {
        return generateRandomColor(Color.WHITE);
    }

    @Override
    public void colorizeSunburst(WeightedTreeItem rootItem) {

    }

    /**
     * Averages the RGB values of a random color with those of a constant color.
     * Returns the averaged random color.
     * Credits to David Crow
     * (http://stackoverflow.com/questions/43044/algorithm-to-randomly-generate-an-aesthetically-pleasing-color-palette)
     * @param mix
     * @return
     */
    private Color generateRandomColor(Color mix) {
        Random random = new Random();

        double red = random.nextDouble();
        double green = random.nextDouble();
        double blue = random.nextDouble();

        // mix the color
        if (mix != null) {
            red =   (red + mix.getRed()) / 2;
            green = (green + mix.getGreen()) / 2;
            blue = (blue + mix.getBlue()) / 2;
        }

        return Color.color(red, green, blue);
    }
}
