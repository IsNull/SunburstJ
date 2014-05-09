package helpers;

import controls.sunburst.WeightedTreeItem;

import java.sql.*;
import java.util.*;

/**
 * Created by n0daft on 09.05.2014.
 */
public class JDBC<T> {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/cosetest";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";

    public static void main(String[] args) {
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



            //STEP 5: Extract data from result set
            while(rs.next()){


                String first = rs.getString("link_from");
                String last = rs.getString("link_to");

                //Display values
                System.out.print(", link_from: " + first);
                System.out.println(", link_to: " + last);
            }

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
    }//end main

    Map<String, WeightedTreeItem<String>> items = new HashMap<>();
    private WeightedTreeItem<String> findOrCreate(String itemName){
        WeightedTreeItem<String> item = items.get(itemName);

        if(item == null){
            item = new WeightedTreeItem<>(2, itemName);
        }

        return item;
    }
}
