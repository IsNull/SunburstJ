package helpers;

import controls.sunburst.WeightedTreeItem;

/**
 * Created by n0daft on 09.05.2014.
 */
public interface ISourceStrategy {

    WeightedTreeItem<String> getData(String databaseName, String user, String password);

}
