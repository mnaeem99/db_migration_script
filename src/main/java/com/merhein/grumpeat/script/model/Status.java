package com.merhein.grumpeat.script.model;

public class Status {
    private Long id;
    private String name;
    private int percentage;

    public Status(Long id, String name, int percentage) {
        this.id = id;
        this.name = name;
        this.percentage = percentage;
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

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
