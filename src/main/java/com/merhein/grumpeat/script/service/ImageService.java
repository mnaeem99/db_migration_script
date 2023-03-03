package com.merhein.grumpeat.script.service;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.merhein.grumpeat.script.driverManager.DatabaseConnection;
import com.merhein.grumpeat.script.model.Image;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ImageService {
    public ArrayList<Image> getImages(String databaseName){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName);
        Statement stmt = null;
        ArrayList<Image> images = new ArrayList<>();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM images;" );
            while ( rs.next() ) {
                long id = rs.getLong("id");
                String imageUrl = rs.getString("image_url");
                String thumbnailUrl = rs.getString("thumbnail_url");
                Image image = new Image(imageUrl,thumbnailUrl,id);
                images.add(image);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        return images;
    }
    public void saveImages(String databaseName, String fromBucket, String toBucket, ArrayList<Image> images){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection c = databaseConnection.getConnection(databaseName);
        try {
            c.setAutoCommit(false);
            PreparedStatement prepStmt = c.prepareStatement(
                    "INSERT INTO images(id,image_url,thumbnail_url) values (?,?,?)");
            for (Image image : images) {
                prepStmt.setLong(1, image.getId());
                if (image.getImageUrl().contains(fromBucket)) {
                    //change s3 bucket url path
                    String imageUrl = image.getImageUrl().replace(fromBucket,toBucket);
                    prepStmt.setString(2, imageUrl);
                }
                else {
                    prepStmt.setString(2, image.getImageUrl());
                }
                prepStmt.setString(3, image.getThumbnailUrl());
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
    public void migrateS3BucketObjects(String fromBucket, String toBucket){
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();
        List<S3ObjectSummary> objects = s3Client.listObjects(fromBucket).getObjectSummaries();
        for (S3ObjectSummary obj : objects){
            s3Client.copyObject(fromBucket,obj.getKey(),toBucket,obj.getKey());
        }
    }

    /**
     * migrate images from one database to another database.
     * @param fromDatabase of the existing database name
     * @param toDatabase of the new database name
     * @param fromBucket of the existing bucket name
     * @param toBucket of the new bucket name
     **/
    public void migrateImages(String fromDatabase, String toDatabase, String fromBucket, String toBucket){
//        migrateS3BucketObjects(fromBucket, toBucket);
        ArrayList<Image> images = getImages(fromDatabase);
        saveImages(toDatabase, fromBucket, toBucket, images);
    }
}
