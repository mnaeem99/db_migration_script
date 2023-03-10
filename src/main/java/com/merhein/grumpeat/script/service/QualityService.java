package com.merhein.grumpeat.script.service;

import com.merhein.grumpeat.script.driverManager.DatabaseConnection;
import com.merhein.grumpeat.script.model.Quality;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class QualityService {
    public ArrayList<Quality> getQualities(String databaseName, String databaseUser, String databasePassword){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        Statement stmt = null;
        ArrayList<Quality> qualities = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM quality;" );
            while ( rs.next() ) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String equivalentQuality = rs.getString("equivalent_quality");
                Long qualityTypeId = rs.getLong("quality_type_id");
                Boolean active = rs.getBoolean("active");
                Quality quality = new Quality(id,name,equivalentQuality,qualityTypeId,active);
                qualities.add(quality);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return qualities;
    }
    public void saveQualities(String databaseName,String databaseUser, String databasePassword, ArrayList<Quality> qualities){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        try {
            c.setAutoCommit(false);
            PreparedStatement prepStmt = c.prepareStatement(
                    "INSERT INTO quality(id,name,equivalent_quality,quality_type_id,active) values (?,?,?,?,?)");
            for (Quality quality : qualities) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM quality where id = "+quality.getId()+";" );
                if (rs==null || rs.wasNull() || !rs.next()) {
                    prepStmt.setLong(1, quality.getId());
                    prepStmt.setString(2, quality.getName());
                    prepStmt.setString(3, quality.getEquivalentQuality());
                    prepStmt.setLong(4, quality.getQualityTypeId());
                    prepStmt.setBoolean(5, quality.getActive());
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
    public void migrateQualities(String fromDatabase, String fromDatabaseUser, String fromDatabasePassword, String toDatabase, String toDatabaseUser, String toDatabasePassword){
        ArrayList<Quality> qualities = getQualities(fromDatabase,fromDatabaseUser,fromDatabasePassword);
        saveQualities(toDatabase,toDatabaseUser,toDatabasePassword, qualities);
    }
}
