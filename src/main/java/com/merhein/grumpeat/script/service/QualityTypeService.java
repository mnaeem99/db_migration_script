package com.merhein.grumpeat.script.service;

import com.merhein.grumpeat.script.driverManager.DatabaseConnection;
import com.merhein.grumpeat.script.model.QualityType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class QualityTypeService {
    public ArrayList<QualityType> getQualityTypes(String databaseName, String databaseUser, String databasePassword){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        Statement stmt = null;
        ArrayList<QualityType> qualityTypes = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM quality_type;" );
            while ( rs.next() ) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                QualityType qualityType = new QualityType(id,name);
                qualityTypes.add(qualityType);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return qualityTypes;
    }
    public void saveQualityTypes(String databaseName, String databaseUser, String databasePassword, ArrayList<QualityType> qualityTypes){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        try {
            c.setAutoCommit(false);
            PreparedStatement prepStmt = c.prepareStatement(
                    "INSERT INTO quality_type(id,name) values (?,?)");
            for (QualityType qualityType : qualityTypes) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM quality_type where id = "+qualityType.getId()+";" );
                if (rs==null || rs.wasNull() || !rs.next()) {
                    prepStmt.setLong(1, qualityType.getId());
                    prepStmt.setString(2, qualityType.getName());
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
    public void migrateQualityTypes(String fromDatabase, String fromDatabaseUser, String fromDatabasePassword, String toDatabase, String toDatabaseUser, String toDatabasePassword){
        ArrayList<QualityType> qualityTypes = getQualityTypes(fromDatabase,fromDatabaseUser,fromDatabasePassword);
        saveQualityTypes(toDatabase,toDatabaseUser,toDatabasePassword, qualityTypes);
    }
}
