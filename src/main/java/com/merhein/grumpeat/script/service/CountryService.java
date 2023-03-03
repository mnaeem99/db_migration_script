package com.merhein.grumpeat.script.service;

import com.merhein.grumpeat.script.driverManager.DatabaseConnection;
import com.merhein.grumpeat.script.model.Country;

import java.sql.*;
import java.util.ArrayList;

public class CountryService {
    public ArrayList<Country> getCountries(String databaseName){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName);
        Statement stmt = null;
        ArrayList<Country> countries = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM country;" );
            while ( rs.next() ) {
                long id = rs.getLong("id");
                String code = rs.getString("code");
                Long pictureId = rs.getLong("picture_id");
                if (rs.wasNull()){
                    pictureId = null;
                }
                Long flagId = rs.getLong("flag_id");
                if (rs.wasNull()){
                    flagId = null;
                }
                Country country = new Country(id,code,pictureId,flagId);
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
    public void saveCountries(String databaseName, ArrayList<Country> countries){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName);
        try {
            c.setAutoCommit(false);
            PreparedStatement prepStmt = c.prepareStatement(
                    "INSERT INTO country(id,code,picture_id,flag_id) values (?,?,?,?)");
            for (Country country : countries) {
                prepStmt.setLong(1, country.getId());
                prepStmt.setString(2, country.getCode());
                setLongOrNull(prepStmt,3, country.getPictureId());
                setLongOrNull(prepStmt, 4, country.getFlagId());
                System.out.println(country.toString());
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
    public void migrateCountries(String fromDatabase, String toDatabase){
        ArrayList<Country> countries = getCountries(fromDatabase);
        saveCountries(toDatabase, countries);
    }
}
