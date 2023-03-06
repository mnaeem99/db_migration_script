package com.merhein.grumpeat.script.service;

import com.merhein.grumpeat.script.driverManager.DatabaseConnection;
import com.merhein.grumpeat.script.model.LocalityTranslation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class LocalityTranslationService {
    public ArrayList<LocalityTranslation> getLocalities(String databaseName, String databaseUser, String databasePassword){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        Statement stmt = null;
        ArrayList<LocalityTranslation> localities = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM locality_translation;" );
            while ( rs.next() ) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String language = rs.getString("language");
                long localityId = rs.getLong("locality_id");
                LocalityTranslation localityTranslation = new LocalityTranslation(id,name,language,localityId);
                localities.add(localityTranslation);
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
    public void saveLocalities(String databaseName, String databaseUser, String databasePassword, ArrayList<LocalityTranslation> localities){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        try {
            c.setAutoCommit(false);
            PreparedStatement prepStmt = c.prepareStatement(
                    "INSERT INTO locality_translation(id,name,language,locality_id) values (?,?,?,?)");
            for (LocalityTranslation locality : localities) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM locality_translation where id = "+locality.getId()+";" );
                if (rs==null || rs.wasNull() || !rs.next()) {
                    prepStmt.setLong(1, locality.getId());
                    prepStmt.setString(2, locality.getName());
                    prepStmt.setString(3, locality.getLanguage());
                    prepStmt.setLong(4, locality.getLocalityId());
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
    public void migrateLocalityTranslation(String fromDatabase, String fromDatabaseUser, String fromDatabasePassword, String toDatabase, String toDatabaseUser, String toDatabasePassword){
        ArrayList<LocalityTranslation> localities = getLocalities(fromDatabase,fromDatabaseUser,fromDatabasePassword);
        saveLocalities(toDatabase,toDatabaseUser,toDatabasePassword, localities);
    }
}
