package com.merhein.grumpeat.script.model;
public class Image {
    private String imageUrl;
    private String thumbnailUrl;
    private Long id;

    public Image(String imageUrl, String thumbnailUrl, Long id) {
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
