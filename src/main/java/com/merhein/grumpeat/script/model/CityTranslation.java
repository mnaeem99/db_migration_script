package com.merhein.grumpeat.script.model;

public class CityTranslation {
    private Long id;
    private String name;
    private String language;
    private Long cityId;

    public CityTranslation(Long id, String name, String language, Long cityId) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.cityId = cityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}
