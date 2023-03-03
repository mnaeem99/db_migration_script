package com.merhein.grumpeat.script.model;

public class Country {
    private Long id;
    private String code;
    private Long pictureId;
    private Long flagId;

    public Country(Long id, String code, Long pictureId, Long flagId) {
        this.id = id;
        this.code = code;
        this.pictureId = pictureId;
        this.flagId = flagId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public Long getFlagId() {
        return flagId;
    }

    public void setFlagId(Long flagId) {
        this.flagId = flagId;
    }
}
