package com.merhein.grumpeat.script.model;

public class LocalityTranslation {
    private Long id;
    private String name;
    private String language;
    private Long localityId;

    public LocalityTranslation(Long id, String name, String language, Long localityId) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.localityId = localityId;
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

    public Long getLocalityId() {
        return localityId;
    }

    public void setLocalityId(Long localityId) {
        this.localityId = localityId;
    }
}
