import controls.skin.SunburstViewSkin;
import controls.sunburst.ColorStrategyRandom;
import controls.sunburst.ColorStrategyShades;
import controls.sunburst.SunburstView;
import controls.sunburst.WeightedTreeItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
        ColorStrategyShades colorStrategyShades = new ColorStrategyShades();

        WeightedTreeItem<String> rootData = getData();

        for (WeightedTreeItem<String> eatable : rootData.getChildrenWeighted()){
            System.out.println(eatable.getValue() + ": " + eatable.getRelativeWeight());
        }

        sunburstView.setRootItem(rootData);
        sunburstView.setColorStrategy(colorStrategyShades);

        Button btnCSRandom = new Button();
        btnCSRandom.setText("Random Color Strategy");
        btnCSRandom.setOnAction(event -> {
           sunburstView.setColorStrategy(colorStrategyRandom);
        });

        Button btnCSShades = new Button();
        btnCSShades.setText("Shades Color Strategy");
        btnCSShades.setOnAction(event -> {
            sunburstView.setColorStrategy(colorStrategyShades);
        });

        pane.setBottom(btnCSRandom);
        pane.setTop(btnCSShades);
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
