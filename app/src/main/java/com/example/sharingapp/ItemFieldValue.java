package com.example.sharingapp;

public class ItemFieldValue {
    private final String title;
    private final String maker;
    private final String description;
    private final String length;
    private final String width;
    private final String height;

    public ItemFieldValue(String title, String maker, String description, String length, String width, String height) {
        this.title = title;
        this.maker = maker;
        this.description = description;
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public String getMaker() {
        return maker;
    }

    public String getDescription() {
        return description;
    }

    public String getLength() {
        return length;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }
}
