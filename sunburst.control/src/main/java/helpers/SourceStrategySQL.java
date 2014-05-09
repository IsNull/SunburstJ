package helpers;

import controls.sunburst.WeightedTreeItem;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by n0daft on 09.05.2014.
 */
public class SourceStrategySQL implements ISourceStrategy {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/cosetest";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";

    @Override
    public WeightedTreeItem<String> getData() {
        WeightedTreeItem<String> root = null;

        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM cosetest.temp__2;";
            ResultSet rs = stmt.executeQuery(sql);

            boolean first = true;
            int count = 0;
            //STEP 5: Extract data from result set
            while(rs.next() && count < 6000){

                WeightedTreeItem<String> from = findOrCreate(rs.getString("link_from"));
                WeightedTreeItem<String> to =  findOrCreate(rs.getString("link_to"));

                if(first){
                    root = from;
                    first = false;
                }

                from.getChildren().add(to);
                count++;
            }

            calculateWeightRecursive(root);

            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return root;
    }

    private void calculateWeightRecursive(WeightedTreeItem<String> item){

        item.setWeight(item.getChildrenWeighted().size());

        for(WeightedTreeItem<String> child : item.getChildrenWeighted()){

           // child.setWeight(child.getChildrenWeighted().size());

            if(!child.isLeaf()){
                calculateWeightRecursive(child);
            }
        }
    }


    private Map<String, WeightedTreeItem<String>> items = new HashMap<>();
    private WeightedTreeItem<String> findOrCreate(String itemName){
        WeightedTreeItem<String> item = items.get(itemName);

        if(item == null){
            item = new WeightedTreeItem<>(2, itemName);
            items.put(itemName, item);
        }

        return item;
    }



}
