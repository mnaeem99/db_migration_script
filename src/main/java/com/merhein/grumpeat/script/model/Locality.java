package com.merhein.grumpeat.script.model;

public class Locality {
    private Long id;
    private String placeId;
    private Long pictureId;
    private Long cityId;

    public Locality(Long id, String placeId, Long pictureId, Long cityId) {
        this.id = id;
        this.placeId = placeId;
        this.pictureId = pictureId;
        this.cityId = cityId;
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

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}
