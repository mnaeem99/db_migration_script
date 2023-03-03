package com.merhein.grumpeat.script.model;

public class Quality {
    private Long id;
    private String name;
    private String equivalentQuality;
    private Long qualityTypeId;
    private Boolean active;

    public Quality(Long id, String name, String equivalentQuality, Long qualityTypeId, Boolean active) {
        this.id = id;
        this.name = name;
        this.equivalentQuality = equivalentQuality;
        this.qualityTypeId = qualityTypeId;
        this.active = active;
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

    public String getEquivalentQuality() {
        return equivalentQuality;
    }

    public void setEquivalentQuality(String equivalentQuality) {
        this.equivalentQuality = equivalentQuality;
    }

    public Long getQualityTypeId() {
        return qualityTypeId;
    }

    public void setQualityTypeId(Long qualityTypeId) {
        this.qualityTypeId = qualityTypeId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
