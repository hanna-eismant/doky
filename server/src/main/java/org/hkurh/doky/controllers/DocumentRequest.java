package org.hkurh.doky.controllers;

public class DocumentRequest {

    private String name;
    private String description;

    public DocumentRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
