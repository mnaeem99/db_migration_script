package com.merhein.grumpeat.script.service;

import com.merhein.grumpeat.script.driverManager.DatabaseConnection;
import com.merhein.grumpeat.script.model.CityTranslation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CityTranslationService {
    public ArrayList<CityTranslation> getCities(String databaseName){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName);
        Statement stmt = null;
        ArrayList<CityTranslation> cities = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM city_translation;" );
            while ( rs.next() ) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String language = rs.getString("language");
                long cityId = rs.getLong("city_id");
                CityTranslation cityTranslation = new CityTranslation(id,name,language,cityId);
                cities.add(cityTranslation);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return cities;
    }
    public void saveCities(String databaseName, ArrayList<CityTranslation> cities){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName);
        try {
            c.setAutoCommit(false);
            PreparedStatement prepStmt = c.prepareStatement(
                    "INSERT INTO city_translation(id,name,language,city_id) values (?,?,?,?)");
            for (CityTranslation city : cities) {
                prepStmt.setLong(1, city.getId());
                prepStmt.setString(2, city.getName());
                prepStmt.setString(3, city.getLanguage());
                prepStmt.setLong(4, city.getCityId());
                prepStmt.addBatch();
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
    public void migrateCityTranslation(String fromDatabase, String toDatabase){
        ArrayList<CityTranslation> cities = getCities(fromDatabase);
        saveCities(toDatabase, cities);
    }
}
