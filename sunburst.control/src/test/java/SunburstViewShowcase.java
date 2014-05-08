import controls.sunburst.ColorStrategyRandom;
import controls.sunburst.ColorStrategySectorShades;
import controls.sunburst.SunburstView;
import controls.sunburst.WeightedTreeItem;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.controlsfx.control.SegmentedButton;

/**
 * Created by IsNull on 17.04.14.
 */
public class SunburstViewShowcase extends  javafx.application.Application {


    /**
     * Launchable
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Sunburst View");

        BorderPane pane = new BorderPane();

        SunburstView sunburstView = new SunburstView();

        ColorStrategyRandom colorStrategyRandom = new ColorStrategyRandom();
        ColorStrategySectorShades colorStrategyShades = new ColorStrategySectorShades();

        WeightedTreeItem<String> rootData = getData();

        for (WeightedTreeItem<String> eatable : rootData.getChildrenWeighted()){
            System.out.println(eatable.getValue() + ": " + eatable.getRelativeWeight());
        }

        sunburstView.setRootItem(rootData);


        // Example Controls

        ToggleButton btnCSRandom = new ToggleButton("Random Color Strategy");
        btnCSRandom.setOnAction(event -> {
           sunburstView.setColorStrategy(colorStrategyRandom);
        });

        ToggleButton btnCSShades = new ToggleButton("Shades Color Strategy");
        btnCSShades.setOnAction(event -> {
            sunburstView.setColorStrategy(colorStrategyShades);
        });

        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(10);
        slider.setValue(sunburstView.getMaxDeepness());
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(5);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(1);

        slider.valueProperty().addListener(x -> sunburstView.setMaxDeepness((int)slider.getValue()));

        HBox toolbar = new HBox(20);
        BorderPane.setMargin(toolbar, new Insets(10));

        SegmentedButton colorStrategies = new SegmentedButton();
        colorStrategies.getButtons().addAll(btnCSRandom, btnCSShades);
        toolbar.getChildren().addAll(colorStrategies, slider);

        pane.setTop(toolbar);

        pane.setCenter(sunburstView);

        stage.setScene(new Scene(pane, 1080, 800));
        stage.show();
    }


    /**
     * Build test data model
     * @return
     */
    private WeightedTreeItem<String> getData(){
        WeightedTreeItem<String> root = new WeightedTreeItem(1, "eatables");

        WeightedTreeItem<String> meat = new WeightedTreeItem(3, "meat");
        WeightedTreeItem<String> potato = new WeightedTreeItem(5, "potato");
        WeightedTreeItem<String> fruits = new WeightedTreeItem(10, "fruits");

        // Fruits
        WeightedTreeItem<String> apples = new WeightedTreeItem(5, "apples");
        WeightedTreeItem<String> pears = new WeightedTreeItem(3, "pears");
        fruits.getChildren().addAll(apples, pears);


        // apples
        WeightedTreeItem<String> goldenGala = new WeightedTreeItem(2, "golden gala");
        WeightedTreeItem<String> goldenDelicious = new WeightedTreeItem(2, "golden delicious");
        WeightedTreeItem<String> elStar = new WeightedTreeItem(2, "El Star");

        apples.getChildren().addAll(goldenGala, goldenDelicious, elStar);

        // pears
        WeightedTreeItem<String> pear1 = new WeightedTreeItem(2, "pear 1");
        WeightedTreeItem<String> pear2 = new WeightedTreeItem(5, "pear 2");

        pears.getChildren().addAll(pear1, pear2);

        // Pear 2 details


        // POTATOS

        WeightedTreeItem<String> potatoOil = new WeightedTreeItem(5, "potato oil");

        WeightedTreeItem<String> frites = new WeightedTreeItem(20, "frites");
        WeightedTreeItem<String> bigfrites = new WeightedTreeItem(7, "bigfrites");
        potatoOil.getChildren().addAll(frites, bigfrites);

        WeightedTreeItem<String> frites_d1 = new WeightedTreeItem(2, "fries 2 D1");
        WeightedTreeItem<String> frites_d2 = new WeightedTreeItem(5, "fries 2 D2");
        WeightedTreeItem<String> frites_d3 = new WeightedTreeItem(3, "fries 2 D3");
        frites.getChildren().addAll(frites_d1, frites_d2, frites_d3);


        WeightedTreeItem<String> potatoCooked = new WeightedTreeItem(5, "potato cooked");
        WeightedTreeItem<String> saltPotato = new WeightedTreeItem(3, "salt potato");
        WeightedTreeItem<String> otherCookedPotato = new WeightedTreeItem(4, "other potato");

        potatoCooked.getChildren().addAll(saltPotato, otherCookedPotato);

        potato.getChildren().addAll(potatoOil, potatoCooked);



        root.getChildren().addAll(fruits, meat, potato);

        return root;
    }

    /**
     * Build test data model
     * @return
     */
    private WeightedTreeItem<String> getDataL1(){
        WeightedTreeItem<String> root = new WeightedTreeItem(1, "eatables");

        WeightedTreeItem<String> meat = new WeightedTreeItem(3, "meat");
        WeightedTreeItem<String> potato = new WeightedTreeItem(5, "potato");
        WeightedTreeItem<String> fruits = new WeightedTreeItem(10, "fruits");


        root.getChildren().addAll(fruits, meat, potato);

        return root;
    }
}
