package com.merhein.grumpeat.script.service;

import com.merhein.grumpeat.script.driverManager.DatabaseConnection;
import com.merhein.grumpeat.script.model.CountryTranslation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CountryTranslationService {
    public ArrayList<CountryTranslation> getCountries(String databaseName, String databaseUser, String databasePassword){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        Statement stmt = null;
        ArrayList<CountryTranslation> countries = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM country_translation;" );
            while ( rs.next() ) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String language = rs.getString("language");
                long countryId = rs.getLong("country_id");
                CountryTranslation country = new CountryTranslation(id,name,language,countryId);
                countries.add(country);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return countries;
    }
    public void saveCountries(String databaseName, String databaseUser, String databasePassword, ArrayList<CountryTranslation> countries){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName,databaseUser,databasePassword);
        try {
            c.setAutoCommit(false);
            PreparedStatement prepStmt = c.prepareStatement(
                    "INSERT INTO country_translation(id,name,language,country_id) values (?,?,?,?)");
            for (CountryTranslation country : countries) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM country_translation where id = "+country.getId()+";" );
                if (rs==null || rs.wasNull() || !rs.next()) {
                    prepStmt.setLong(1, country.getId());
                    prepStmt.setString(2, country.getName());
                    prepStmt.setString(3, country.getLanguage());
                    prepStmt.setLong(4, country.getCountryId());
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
    public void migrateCountryTranslation(String fromDatabase, String fromDatabaseUser, String fromDatabasePassword, String toDatabase, String toDatabaseUser, String toDatabasePassword){
        ArrayList<CountryTranslation> countries = getCountries(fromDatabase,fromDatabaseUser,fromDatabasePassword);
        saveCountries(toDatabase,toDatabaseUser,toDatabasePassword, countries);
    }
}
