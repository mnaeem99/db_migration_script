package com.merhein.grumpeat.script.service;

import com.merhein.grumpeat.script.driverManager.DatabaseConnection;
import com.merhein.grumpeat.script.model.City;

import java.sql.*;
import java.util.ArrayList;

public class CityService {
    public ArrayList<City> getCities(String databaseName, String databaseUser, String databasePassword){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        Statement stmt = null;
        ArrayList<City> cities = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM city;" );
            while ( rs.next() ) {
                long id = rs.getLong("id");
                String placeId = rs.getString("place_id");
                Long pictureId = rs.getLong("picture_id");
                if (rs.wasNull()){
                    pictureId = null;
                }
                Long countryId = rs.getLong("country_id");
                if (rs.wasNull()){
                    countryId = null;
                }
                City city = new City(id,placeId,pictureId,countryId);
                cities.add(city);
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
    public void saveCities(String databaseName, String databaseUser, String databasePassword, ArrayList<City> cities){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        try {
            c.setAutoCommit(false);
            PreparedStatement prepStmt = c.prepareStatement(
                    "INSERT INTO city(id,place_id,picture_id,country_id) values (?,?,?,?)");
            for (City city : cities) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM city where id = "+city.getId()+";" );
                if (rs==null || rs.wasNull() || !rs.next()) {
                    prepStmt.setLong(1, city.getId());
                    prepStmt.setString(2, city.getPlaceId());
                    setLongOrNull(prepStmt, 3, city.getPictureId());
                    setLongOrNull(prepStmt, 4, city.getCountryId());
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
    public static void setLongOrNull(PreparedStatement ps, int index, Long value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.BIGINT);
        } else {
            ps.setLong(index, value);
        }
    }
    /**
     * migrate country from one database to another database.
     * @param fromDatabase of the existing database name
     * @param toDatabase of the new database name
     **/
    public void migrateCities(String fromDatabase, String fromDatabaseUser, String fromDatabasePassword, String toDatabase, String toDatabaseUser, String toDatabasePassword){
        ArrayList<City> cities = getCities(fromDatabase,fromDatabaseUser,fromDatabasePassword);
        saveCities(toDatabase,toDatabaseUser,toDatabasePassword, cities);
    }
}
