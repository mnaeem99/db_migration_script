package com.merhein.grumpeat.script.service;

import com.merhein.grumpeat.script.driverManager.DatabaseConnection;
import com.merhein.grumpeat.script.model.RestaurantPrice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class RestaurantPriceService {
    public ArrayList<RestaurantPrice> getRestaurantPrices(String databaseName, String databaseUser, String databasePassword){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        Statement stmt = null;
        ArrayList<RestaurantPrice> restaurantPrices = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM restaurant_price;" );
            while ( rs.next() ) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                RestaurantPrice restaurantPrice = new RestaurantPrice(id,name);
                restaurantPrices.add(restaurantPrice);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return restaurantPrices;
    }
    public void saveRestaurantPrices(String databaseName, String databaseUser, String databasePassword, ArrayList<RestaurantPrice> restaurantPrices){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        try {
            c.setAutoCommit(false);
            PreparedStatement prepStmt = c.prepareStatement(
                    "INSERT INTO restaurant_price(id,name) values (?,?)");
            for (RestaurantPrice restaurantPrice : restaurantPrices) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM restaurant_price where id = "+restaurantPrice.getId()+";" );
                if (rs==null || rs.wasNull() || !rs.next()) {
                    prepStmt.setLong(1, restaurantPrice.getId());
                    prepStmt.setString(2, restaurantPrice.getName());
                    prepStmt.addBatch();
                }
            }
            int [] numUpdates=prepStmt.executeBatch();
            for (int i=0; i < numUpdates.length; i++) {
                if (numUpdates[i] == -2)
                    System.out.println("Execution " + i + ": unknown number of rows updated");
                else
                    System.out.println("Execution " + i + "successful: " + numUpdates[i] + " rows updated");
            }
            c.commit();
            prepStmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
    }

    /**
     * migrate country from one database to another database.
     * @param fromDatabase of the existing database name
     * @param toDatabase of the new database name
     **/
    public void migrateRestaurantPrices(String fromDatabase, String fromDatabaseUser, String fromDatabasePassword, String toDatabase, String toDatabaseUser, String toDatabasePassword){
        ArrayList<RestaurantPrice> restaurantPrices = getRestaurantPrices(fromDatabase,fromDatabaseUser,fromDatabasePassword);
        saveRestaurantPrices(toDatabase,toDatabaseUser,toDatabasePassword, restaurantPrices);
    }
}
