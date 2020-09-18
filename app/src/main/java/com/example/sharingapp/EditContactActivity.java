package com.example.sharingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

/**
 * Editing a pre-existing contact consist of deleting the old contact and adding a new contact with the old contact's id
 * Note: You will not be able to delete contact which are "active" borrowers
 */
public class EditContactActivity extends AppCompatActivity implements Observer {

    private ContactList contact_list = new ContactList();
    private ContactListController contact_list_controller = new ContactListController(contact_list);

    private Contact contact;
    private ContactController contact_controller;

    private Context context;
    private EditText username;
    private EditText email;

    private int pos;
    private boolean on_create_update = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        Intent intent = getIntent();
        pos = intent.getIntExtra("position", 0);

        context = getApplicationContext();
        contact_list_controller.addObserver(this);
        contact_list_controller.loadContacts(context);

        on_create_update = false;
    }

    public void saveContact(View view) {
        String username_str = username.getText().toString();
        String email_str = email.getText().toString();

        if(!validateInput(username_str, email_str)) {
            return;
        }

        String id = contact_controller.getId(); // Reuse the contact id
        Contact updated_contact = new Contact(username_str, email_str, id);

        // Edit contact: replace contact with updated contact
        boolean success = contact_list_controller.editContact(contact, updated_contact, context);
        if(!success) {
            return;
        }

        // End EditContactActivity
        finish();
    }

    private boolean validateInput(String username_str, String email_str) {
        if(email_str.equals("")) {
            email.setError("Empty field!");
            return false;
        }

        if(!email_str.contains("@")) {
            email.setError("Must be an email address!");
            return false;
        }

        // Check that username is unique AND username is changed (Note: if username was not changed
        // then this should be fine, because it was already unique.)
        if(!contact_list_controller.isUsernameAvailable(username_str) && !(contact_controller.getUsername().equals(username_str))) {
            username.setError("Username already taken!");
            return false;
        }

        return true;
    }

    public void deleteContact(View view) {
        // Delete contact
        boolean success = contact_list_controller.deleteContact(contact, context);
        if(!success) {
            return;
        }

        // End EditContactActivity
        finish();
    }

    /**
     * Only needs to update the view from the onCreate method
     */
    @Override
    public void update() {
        if(on_create_update) {

            contact = contact_list_controller.getContact(pos);
            contact_controller = new ContactController(contact);

            username = (EditText) findViewById(R.id.username);
            email = (EditText) findViewById(R.id.email);

            // Update the view
            contact_controller.updateContact(username, email);
        }
    }

    /**
     * Called when the activity is destroyed, thus we remove this activity as a listener
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        contact_list_controller.removeObserver(this);
    }
}