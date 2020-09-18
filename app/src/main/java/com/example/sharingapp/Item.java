package com.example.sharingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * Item class
 */
public class Item extends Observable {

    private String title;
    private String maker;
    private String description;
    private Dimensions dimensions;
    private String status;
    private Contact borrower;
    protected transient Bitmap image;
    protected String image_base64;
    private String id;

    public Item(String title, String maker, String description, Bitmap image, String id) {
        this.title = title;
        this.maker = maker;
        this.description = description;
        this.dimensions = null;
        this.status = "Available";
        this.borrower = null;
        addImage(image);

        if (id == null){
            setId();
        } else {
            updateId(id);
        }
    }

    public String getId(){
        return this.id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
        notifyObservers();
    }

    public void updateId(String id){
        this.id = id;
        notifyObservers();
    }

    public void setTitle(String title) {
        this.title = title;
        notifyObservers();
    }

    public String getTitle() {
        return title;
    }

    public void setMaker(String maker) {
        this.maker = maker;
        notifyObservers();
    }

    public String getMaker() {
        return maker;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyObservers();
    }

    public String getDescription() {
        return description;
    }

    public void setDimensions(String length, String width, String height) {
        this.dimensions = new Dimensions(length, width, height);
        notifyObservers();
    }

    public String getLength() {
        return dimensions.getLength();
    }

    public String getWidth() {
        return dimensions.getWidth();
    }

    public String getHeight() {
        return dimensions.getHeight();
    }

    public void setStatus(String status) {
        this.status = status;
        notifyObservers();
    }

    public String getStatus() {
        return status;
    }

    public void setBorrower(Contact borrower) {
        this.borrower = borrower;
        notifyObservers();
    }

    public Contact getBorrower() {
        return borrower;
    }

    public void addImage(Bitmap new_image){
        if (new_image != null) {
            image = new_image;
            ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
            new_image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
            byte[] b = byteArrayBitmapStream.toByteArray();
            image_base64 = Base64.encodeToString(b, Base64.DEFAULT);
        }
        notifyObservers();
    }

    public Bitmap getImage(){
        if (image == null && image_base64 != null) {
            byte[] decodeString = Base64.decode(image_base64, Base64.DEFAULT);
            image = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        }
        return image;
    }

    public void updateItem(ItemView itemView) {
        itemView.getTitle().setText(getTitle());
        itemView.getMaker().setText(getMaker());
        itemView.getDescription().setText(getDescription());
        itemView.getLength().setText(getLength());
        itemView.getWidth().setText(getWidth());
        itemView.getHeight().setText(getHeight());

        Bitmap image = getImage();
        if(image != null) {
            itemView.getPhoto().setImageBitmap(image);
        } else {
            itemView.getPhoto().setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    public void setItemVisibility(Switch status, TextView borrower_tv, Spinner borrower_spinner) {
        if(getStatus().equals("Borrowed")) {
            status.setChecked(false);
        } else {
            borrower_tv.setVisibility(View.GONE);
            borrower_spinner.setVisibility(View.GONE);
        }
    }
}

