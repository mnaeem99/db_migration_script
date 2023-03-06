package com.merhein.grumpeat.script.service;

import com.merhein.grumpeat.script.driverManager.DatabaseConnection;
import com.merhein.grumpeat.script.model.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class StatusService {
    public ArrayList<Status> getStatuses(String databaseName, String databaseUser, String databasePassword){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        Statement stmt = null;
        ArrayList<Status> statuses = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM status;" );
            while ( rs.next() ) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                int percentage = rs.getInt("percentage");
                Status city = new Status(id,name,percentage);
                statuses.add(city);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return statuses;
    }
    public void saveStatuses(String databaseName, String databaseUser, String databasePassword, ArrayList<Status> statuses){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        try {
            c.setAutoCommit(false);
            PreparedStatement prepStmt = c.prepareStatement(
                    "INSERT INTO status(id,name,percentage) values (?,?,?)");
            for (Status status : statuses) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM status where id = "+status.getId()+";" );
                if (rs==null || rs.wasNull() || !rs.next()) {
                    prepStmt.setLong(1, status.getId());
                    prepStmt.setString(2, status.getName());
                    prepStmt.setInt(3, status.getPercentage());
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
    public void migrateStatuses(String fromDatabase, String fromDatabaseUser, String fromDatabasePassword, String toDatabase, String toDatabaseUser, String toDatabasePassword){
        ArrayList<Status> statuses = getStatuses(fromDatabase,fromDatabaseUser,fromDatabasePassword);
        saveStatuses(toDatabase,toDatabaseUser,toDatabasePassword, statuses);
    }
}
