package com.merhein.grumpeat.script.service;

import com.merhein.grumpeat.script.driverManager.DatabaseConnection;
import com.merhein.grumpeat.script.model.Locality;

import java.sql.*;
import java.util.ArrayList;

public class LocalityService {
    public ArrayList<Locality> getLocalities(String databaseName){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName);
        Statement stmt = null;
        ArrayList<Locality> localities = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM locality;" );
            while ( rs.next() ) {
                long id = rs.getLong("id");
                String placeId = rs.getString("place_id");
                Long pictureId = rs.getLong("picture_id");
                if (rs.wasNull()){
                    pictureId = null;
                }
                Long cityId = rs.getLong("city_id");
                if (rs.wasNull()){
                    cityId = null;
                }
                Locality city = new Locality(id,placeId,pictureId,cityId);
                localities.add(city);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return localities;
    }
    public void saveLocalities(String databaseName, ArrayList<Locality> localities){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName);
        try {
            c.setAutoCommit(false);
            PreparedStatement prepStmt = c.prepareStatement(
                    "INSERT INTO locality(id,place_id,picture_id,city_id) values (?,?,?,?)");
            for (Locality locality : localities) {
                prepStmt.setLong(1, locality.getId());
                prepStmt.setString(2, locality.getPlaceId());
                setLongOrNull(prepStmt,3, locality.getPictureId());
                setLongOrNull(prepStmt, 4, locality.getCityId());
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
    public void migrateLocalities(String fromDatabase, String toDatabase){
        ArrayList<Locality> localities = getLocalities(fromDatabase);
        saveLocalities(toDatabase, localities);
    }
}
