package com.merhein.grumpeat.script;

import com.merhein.grumpeat.script.service.*;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner input = new Scanner(System.in);

        System.out.println("-----------------------Details of copied from database-----------------------");
        System.out.print("Enter database name: ");
        String fromDatabaseName = input.nextLine();
        System.out.print("Enter database user: ");
        String fromDatabaseUser = input.nextLine();
        System.out.print("Enter database password: ");
        String fromDatabasePassword = input.nextLine();

        System.out.println("\n\n-----------------------Details of copied to database-----------------------");
        System.out.print("Enter database name: ");
        String toDatabaseName = input.nextLine();
        System.out.print("Enter database user: ");
        String toDatabaseUser = input.nextLine();
        System.out.print("Enter database password: ");
        String toDatabasePassword = input.nextLine();

        /* migrate images from one database to another database. */
        ImageService imageService = new ImageService();
        imageService.migrateImages(fromDatabaseName,fromDatabaseUser,fromDatabasePassword,toDatabaseName,toDatabaseUser,toDatabasePassword, "grumpeat-dev","grumpeat-staging");


        /* migrate country from one database to another database. */
        CountryService countryService = new CountryService();
        countryService.migrateCountries(fromDatabaseName,fromDatabaseUser,fromDatabasePassword,toDatabaseName,toDatabaseUser,toDatabasePassword);


        /* migrate countryTranslation from one database to another database. */
        CountryTranslationService countryTranslationService = new CountryTranslationService();
        countryTranslationService.migrateCountryTranslation(fromDatabaseName,fromDatabaseUser,fromDatabasePassword,toDatabaseName,toDatabaseUser,toDatabasePassword);


        /* migrate city from one database to another database. */
        CityService cityService = new CityService();
        cityService.migrateCities(fromDatabaseName,fromDatabaseUser,fromDatabasePassword,toDatabaseName,toDatabaseUser,toDatabasePassword);


        /* migrate cityTranslation from one database to another database. */
        CityTranslationService cityTranslationService = new CityTranslationService();
        cityTranslationService.migrateCityTranslation(fromDatabaseName,fromDatabaseUser,fromDatabasePassword,toDatabaseName,toDatabaseUser,toDatabasePassword);


        /* migrate locality from one database to another database. */
        LocalityService localityService = new LocalityService();
        localityService.migrateLocalities(fromDatabaseName,fromDatabaseUser,fromDatabasePassword,toDatabaseName,toDatabaseUser,toDatabasePassword);


        /* migrate localityTranslation from one database to another database. */
        LocalityTranslationService localityTranslationService = new LocalityTranslationService();
        localityTranslationService.migrateLocalityTranslation(fromDatabaseName,fromDatabaseUser,fromDatabasePassword,toDatabaseName,toDatabaseUser,toDatabasePassword);


        /* migrate status from one database to another database. */
        StatusService statusService = new StatusService();
        statusService.migrateStatuses(fromDatabaseName,fromDatabaseUser,fromDatabasePassword,toDatabaseName,toDatabaseUser,toDatabasePassword);


        /* migrate price level from one database to another database. */
        RestaurantPriceService restaurantPriceService = new RestaurantPriceService();
        restaurantPriceService.migrateRestaurantPrices(fromDatabaseName,fromDatabaseUser,fromDatabasePassword,toDatabaseName,toDatabaseUser,toDatabasePassword);


        /* migrate quality type from one database to another database. */
        QualityTypeService qualityTypeService = new QualityTypeService();
        qualityTypeService.migrateQualityTypes(fromDatabaseName,fromDatabaseUser,fromDatabasePassword,toDatabaseName,toDatabaseUser,toDatabasePassword);


        /* migrate quality from one database to another database. */
        QualityService qualityService = new QualityService();
        qualityService.migrateQualities(fromDatabaseName,fromDatabaseUser,fromDatabasePassword,toDatabaseName,toDatabaseUser,toDatabasePassword);

    }
}
