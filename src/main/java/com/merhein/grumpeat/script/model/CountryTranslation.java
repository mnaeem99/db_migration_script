package com.merhein.grumpeat.script.model;

public class CountryTranslation {
    private Long id;
    private String name;
    private String language;
    private Long countryId;

    public CountryTranslation(Long id, String name, String language, Long countryId) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.countryId = countryId;
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

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
}
