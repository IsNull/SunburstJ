import controls.sunburst.SunburstView;
import controls.sunburst.WeightedTreeItem;
import javafx.scene.Scene;
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

        WeightedTreeItem<String> rootData = getData();

        for (WeightedTreeItem<String> eatable : rootData.getChildrenWeighted()){
            System.out.println(eatable.getValue() + ": " + eatable.getRelativeWeight());
        }

        sunburstView.setRootItem(rootData);

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

        WeightedTreeItem<String> fruits = new WeightedTreeItem(10, "fruits");

        WeightedTreeItem<String> apples = new WeightedTreeItem(5, "apples");
        WeightedTreeItem<String> pears = new WeightedTreeItem(5, "pears");
        fruits.getChildren().addAll(apples, pears);


        WeightedTreeItem<String> meat = new WeightedTreeItem(3, "meat");
        WeightedTreeItem<String> potato = new WeightedTreeItem(5, "potato");

        root.getChildren().addAll(fruits, meat, potato);

        return root;
    }
}
