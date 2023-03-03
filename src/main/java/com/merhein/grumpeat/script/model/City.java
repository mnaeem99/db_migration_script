package com.merhein.grumpeat.script.model;

public class City {
    private Long id;
    private String placeId;
    private Long pictureId;
    private Long countryId;

    public City(Long id, String placeId, Long pictureId, Long countryId) {
        this.id = id;
        this.placeId = placeId;
        this.pictureId = pictureId;
        this.countryId = countryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
}
