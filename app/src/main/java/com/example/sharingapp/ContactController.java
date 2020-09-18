package com.example.sharingapp;

import android.widget.EditText;

/**
 * ContactController is responsible for all communication between views and contact object
 */
public class ContactController {
    private Contact contact;

    public ContactController(Contact contact) {
        this.contact = contact;
    }

    public String getId() {
        return contact.getId();
    }

    public void setId() {
        contact.setId();
    }

    public void updateId(String id) {
        contact.updateId(id);
    }

    public void setUsername(String username) {
        contact.setUsername(username);
    }

    public String getUsername() {
        return contact.getUsername();
    }

    public void setEmail(String email) {
        contact.setEmail(email);
    }

    public String getEmail() {
        return contact.getEmail();
    }
	
	public Contact getContact() {
		return this.contact;
	}

	public void updateContact(EditText username, EditText email) {
        contact.updateContact(username, email);
    }

	public void addObserver(Observer observer) {
        contact.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        contact.removeObserver(observer);
    }
}