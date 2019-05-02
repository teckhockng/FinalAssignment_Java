
package Views;

import Model.Camera;
import Model.Electronics;
import Model.Laptop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;



/**
 * FXML Controller class
 *
 * @author 
 */
public class TableOfProductsController implements Initializable {
    @FXML private TableView<Electronics> productTableView;
    @FXML private TableColumn<Electronics, Integer> productIDColumn;
    @FXML private TableColumn<Electronics, String> manufacturerColumn;
    @FXML private TableColumn<Electronics, String> productTypeColumn;
    @FXML private TableColumn<Electronics, String> descriptionColumn;
    @FXML private TableColumn<Electronics, Double> priceColumn;
    @FXML private CheckBox cameraCheckBox;
    @FXML private CheckBox laptopCheckBox;
    @FXML private TextField searchTextBox;
    @FXML private Label rowsReturnedLabel;
    /**
     * Initializes the controller class.
     * 
     *     private String productName, description, manufacturer;
    private double price;
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        productIDColumn.setCellValueFactory(new PropertyValueFactory<Electronics, Integer>("ProductID"));
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<Electronics, String>("Manufacturer"));
        productTypeColumn.setCellValueFactory(new PropertyValueFactory<Electronics, String>("ProductType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Electronics, String>("Description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Electronics, Double>("Price"));
        cameraCheckBox.setSelected(true);
        laptopCheckBox.setSelected(true);
        
        try {
            
            updateTableFromDB();
            
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }    
    
    /**
     * This method will update the table without any filters
     * @throws SQLException 
     */
    public void updateTableFromDB() throws SQLException
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
                
                electronics.add(newElectronic);
                rowsReturned ++;
            }     
                              
            productTableView.getItems().addAll(electronics);
            rowsReturnedLabel.setText(String.format("Rows returned: %d", rowsReturned));
            
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
    
    
    /**
     * This method will update the table with filters
     * @throws SQLException 
     */
    public void updateTableWithSearchFilter() throws SQLException
    {
        int rowsReturned = 0;
        ObservableList<Electronics> electronics = FXCollections.observableArrayList();
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String sql = "";
        String searchTerm = "";
        searchTerm = searchTextBox.getText();
        productTableView.getItems().clear();
        electronics.clear();
        
        try
    {
        //1. Connect to the database
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/comp1011Test3?useSSL=false","student","student");
        
        //2. Create the statement
        statement = conn.createStatement();
        
        //3. create the SQL
        if(!cameraCheckBox.isSelected()){
            sql = "SELECT * FROM electronics WHERE productType = 'laptop'";

            
        }
        else if(!laptopCheckBox.isSelected()){
            sql = "SELECT * FROM electronics WHERE productType = 'camera'";

            
        }
        else
        {
            sql = "SELECT * FROM electronics";

        }
        
        
        
        
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
                if(resultSet.getString("description").contains(searchTerm)){
                    electronics.add(newElectronic);
                    rowsReturned ++;
                    System.out.printf("The search term is %s", searchTerm);

            
                }
                
                
            }     
            
           if((laptopCheckBox.isSelected() == false) && (cameraCheckBox.isSelected() == false))
        {
            productTableView.getItems().clear();
            electronics.clear();
        }
            
            productTableView.getItems().addAll(electronics);
            rowsReturnedLabel.setText(String.format("Rows returned: %d", rowsReturned));
            
        
            
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
