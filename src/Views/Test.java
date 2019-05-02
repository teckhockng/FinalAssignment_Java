
package Views;

import Model.Electronics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Teck Hock
 */
public class Test {
    
    public static void main(String[] args)
    {
        try {
            updateTableFromDB();
            
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This method will update the table without any filters
     * @throws SQLException 
     */
    public static void updateTableFromDB() throws SQLException
    {
        int rowsReturned = 0;
        ObservableList<Electronics> electronics = FXCollections.observableArrayList();
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try
    {
        //1. Connect to the database
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp1011Test3?useSSL=false","student","student");
        
        //2. Create the statement
        statement = conn.createStatement();
        
        //3. create the SQL
        String sql = "SELECT * FROM electronics";
        
        //4. Query the DB and store the results
        resultSet = statement.executeQuery(sql);
        
        while (resultSet.next())
            {
                Electronics newElectronic = new Electronics(resultSet.getString("productName"),
                                                resultSet.getString("productType"),
                                                resultSet.getString("description"),
                                                resultSet.getString("manufacturer"),
                                                resultSet.getDouble("price")) {
                    @Override
                    public void insertIntoDB() throws SQLException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
                    
                
                newElectronic.setProductID(resultSet.getInt("productID"));
                
                System.out.printf("The product %s is %s", resultSet.getString("productName"), resultSet.getString("productType"));
                
                electronics.add(newElectronic);
                rowsReturned ++;
            }     
                              
            
        } catch (Exception e)
        {
            System.err.println(e);
        }
        finally
        {
            if (conn != null)
                conn.close();
            if(statement != null)
                statement.close();
            if(resultSet != null)
                resultSet.close();
        }
    }
    
}
