import controls.sunburst.DonutUnit;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by IsNull on 17.04.14.
 */
public class SimpleFXShowcase extends  javafx.application.Application {


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

        DonutUnit donut = new DonutUnit();
        donut.setDegreeStart(100);
        donut.setDegreeEnd(90);
        donut.refresh();

        donut.setOnMouseClicked((x) -> {
            System.out.println("you clicked a Donut-Unit!");
            donut.setDegreeEnd(donut.getDegreeEnd() + 15);
            donut.refresh();
        });

        pane.setCenter(donut);

        stage.setScene(new Scene(pane, 1080, 800));
        stage.show();
    }
}
