import controls.sunburst.*;
import helpers.ISourceStrategy;
import helpers.SourceStrategyMockup;
import helpers.SourceStrategySQL;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.SegmentedButton;

import java.util.ArrayList;
import java.util.List;

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

        //ISourceStrategy sourceStrategy = new SourceStrategyMockup();
        ISourceStrategy sourceStrategy = new SourceStrategySQL();

        WeightedTreeItem<String> rootData = sourceStrategy.getData();

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

        ToggleButton btnShowLegend = new ToggleButton("Show Legend");
        btnShowLegend.setSelected(true);
        btnShowLegend.setOnAction(event -> {
            sunburstView.setLegendVisibility(true);
        });

        ToggleButton btnHideLegend = new ToggleButton("Hide Legend");
        btnHideLegend.setOnAction(event -> {
            sunburstView.setLegendVisibility(false);
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

        SegmentedButton legendVisibility = new SegmentedButton();
        legendVisibility.getButtons().addAll(btnShowLegend, btnHideLegend);

        toolbar.getChildren().addAll(colorStrategies, slider, legendVisibility);

        pane.setTop(toolbar);

        pane.setCenter(sunburstView);

        stage.setScene(new Scene(pane, 1080, 800));
        stage.show();
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
