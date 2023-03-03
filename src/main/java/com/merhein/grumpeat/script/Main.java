package com.merhein.grumpeat.script;

import com.merhein.grumpeat.script.service.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {


        /* migrate images from one database to another database. */
        ImageService imageService = new ImageService();
        imageService.migrateImages("grumpeat", "test", "grumpeat-dev","grumpeat-staging");


        /* migrate country from one database to another database. */
        CountryService countryService = new CountryService();
        countryService.migrateCountries("grumpeat", "test");


        /* migrate countryTranslation from one database to another database. */
        CountryTranslationService countryTranslationService = new CountryTranslationService();
        countryTranslationService.migrateCountryTranslation("grumpeat", "test");


        /* migrate city from one database to another database. */
        CityService cityService = new CityService();
        cityService.migrateCities("grumpeat", "test");


        /* migrate cityTranslation from one database to another database. */
        CityTranslationService cityTranslationService = new CityTranslationService();
        cityTranslationService.migrateCityTranslation("grumpeat", "test");


        /* migrate locality from one database to another database. */
        LocalityService localityService = new LocalityService();
        localityService.migrateLocalities("grumpeat", "test");


        /* migrate localityTranslation from one database to another database. */
        LocalityTranslationService localityTranslationService = new LocalityTranslationService();
        localityTranslationService.migrateLocalityTranslation("grumpeat", "test");


        /* migrate status from one database to another database. */
        StatusService statusService = new StatusService();
        statusService.migrateStatuses("grumpeat", "test");


        /* migrate price level from one database to another database. */
        RestaurantPriceService restaurantPriceService = new RestaurantPriceService();
        restaurantPriceService.migrateRestaurantPrices("grumpeat", "test");


        /* migrate quality type from one database to another database. */
        QualityTypeService qualityTypeService = new QualityTypeService();
        qualityTypeService.migrateQualityTypes("grumpeat", "test");


        /* migrate quality from one database to another database. */
        QualityService qualityService = new QualityService();
        qualityService.migrateQualities("grumpeat", "test");

    }
}
