package com.example.sharingapp;

import android.widget.EditText;
import android.widget.ImageView;

public class ItemView {
    private final EditText title;
    private final EditText maker;
    private final EditText description;
    private final EditText length;
    private final EditText width;
    private final EditText height;
    private final ImageView photo;

    public ItemView(EditText title, EditText maker, EditText description, EditText length, EditText width, EditText height, ImageView photo) {
        this.title = title;
        this.maker =  maker;
        this.description = description;
        this.length = length;
        this.width = width;
        this.height = height;
        this.photo = photo;
    }

    public EditText getTitle() {
        return title;
    }
    public EditText getMaker() {
        return maker;
    }

    public EditText getDescription() {
        return description;
    }

    public EditText getLength() {
        return length;
    }

    public EditText getWidth() {
        return width;
    }

    public EditText getHeight() {
        return height;
    }

    public ImageView getPhoto() {
        return photo;
    }
}
