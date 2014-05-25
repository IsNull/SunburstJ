package data;

import controls.sunburst.WeightedTreeItem;

/**
 * Represents a strategy by which the data for the control is received.
 * Created by n0daft on 09.05.2014.
 */
public interface ISourceStrategy {

    /**
     * Returns the data model represented by the root item.
     * We expect the model to be a tree model.
     * @param databaseName
     * @param user
     * @param password
     * @return
     */
    WeightedTreeItem<String> getData(String databaseName, String user, String password);

}
