package com.example.sharingapp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Editing a pre-existing item consists of deleting the old item and adding a new item with the old
 * item's id.
 * Note: invisible EditText is used to setError for status. for whatever reason we cannot setError to
 * the status Switch so instead an error is set to an "invisible" EditText.
 */
public class EditItemActivity extends AppCompatActivity implements Observer {

    private ItemList item_list = new ItemList();
    private ItemListController item_list_controller = new ItemListController(item_list);
    private Item item;
    private ItemController item_controller;

    private Context context;
    private Contact contact;
    private ContactList contact_list = new ContactList();
    private ContactListController contact_list_controller = new ContactListController(contact_list);

    private Bitmap image;
    private int REQUEST_CODE = 1;
    private ImageView photo;
    private ItemView itemView;
    private ItemFieldValue itemFieldValue;

    private EditText title;
    private EditText maker;
    private EditText description;
    private EditText length;
    private EditText width;
    private EditText height;

    private Spinner borrower_spinner;
    private TextView  borrower_tv;
    private Switch status;
    private EditText invisible;

    private boolean on_create_update = false;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        title = (EditText) findViewById(R.id.title);
        maker = (EditText) findViewById(R.id.maker);
        description = (EditText) findViewById(R.id.description);
        length = (EditText) findViewById(R.id.length);
        width = (EditText) findViewById(R.id.width);
        height = (EditText) findViewById(R.id.height);
        photo = (ImageView) findViewById(R.id.image_view);

        itemView = new ItemView(title, maker, description, length, width, height, photo);

        borrower_spinner = (Spinner) findViewById(R.id.borrower_spinner);
        borrower_tv = (TextView) findViewById(R.id.borrower_tv);
        status = (Switch) findViewById(R.id.available_switch);
        invisible = (EditText) findViewById(R.id.invisible);

        invisible.setVisibility(View.GONE);
        Intent intent = getIntent(); // Get intent from ItemsFragment
        pos = intent.getIntExtra("position", 0);

        context = getApplicationContext();

        item_list_controller.addObserver(this);
        item_list_controller.loadItems(context);

        on_create_update = true;

        contact_list_controller.addObserver(this);
        contact_list_controller.loadContacts(context);

        on_create_update = false;
    }

    public void addPhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    public void deletePhoto(View view) {
        image = null;
        photo.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    @Override
    protected void onActivityResult(int request_code, int result_code, Intent intent){
        if (request_code == REQUEST_CODE && result_code == RESULT_OK){
            Bundle extras = intent.getExtras();
            image = (Bitmap) extras.get("data");
            photo.setImageBitmap(image);
        }
    }

    public void deleteItem(View view) {
        // Delete item
        boolean success = item_list_controller.deleteItem(item, context);
        if(!success) {
            return;
        }

        // End EditItemActivity
        item_list_controller.removeObserver(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void saveItem(View view) {
        String title_str = title.getText().toString();
        String maker_str = maker.getText().toString();
        String description_str = description.getText().toString();
        String length_str = length.getText().toString();
        String width_str = width.getText().toString();
        String height_str = height.getText().toString();

        itemFieldValue = new ItemFieldValue(title_str, maker_str, description_str, length_str, width_str, height_str);

        if(!validateInput()) {
            return;
        }

        String id = item_controller.getId(); // Reuse the item id
        Item updated_item = new Item(itemFieldValue.getTitle(), itemFieldValue.getWidth(), itemFieldValue.getDescription(), image, id);
        ItemController updated_item_controller = new ItemController(updated_item);
        updated_item_controller.setDimensions(itemFieldValue.getLength(), itemFieldValue.getWidth(), itemFieldValue.getHeight());

        if(!status.isChecked()) {
            updated_item_controller.setStatus("Borrowed");
            updated_item_controller.setBorrower(contact);
        }

        // Edit item
        boolean success = item_list_controller.editItem(item, updated_item, context);
        if(!success) {
            return;
        }

        // End EditItemActivity
        item_list_controller.removeObserver(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private boolean validateInput() {
        if(!status.isChecked()) {
            String borrower_str = borrower_spinner.getSelectedItem().toString();
            contact = contact_list_controller.getContactByUsername(borrower_str);
        }
        return validateField(itemFieldValue.getTitle(), itemView.getTitle())
               && validateField(itemFieldValue.getMaker(), itemView.getMaker())
               && validateField(itemFieldValue.getDescription(), itemView.getDescription())
               && validateField(itemFieldValue.getLength(), itemView.getLength())
               && validateField(itemFieldValue.getWidth(), itemView.getWidth())
               && validateField(itemFieldValue.getHeight(), itemView.getHeight());
    }

    private boolean validateField(String fieldValue, EditText editText) {
        if(fieldValue.equals("")) {
            editText.setError("Empty field!");
            return false;
        }
        return true;
    }

    /**
     * Checked = Available
     * Unchecked = Borrowed
     */
    public void toggleSwitch(View view) {
        if (status.isChecked()) {
            // Means was previously borrowed, switch was toggled to available
            borrower_spinner.setVisibility(View.GONE);
            borrower_tv.setVisibility(View.GONE);
            item_controller.setBorrower(null);
            item_controller.setStatus("Available");

        } else {
            // Means not borrowed
            if (contact_list.getSize() == 0) {
                // No contacts, need to add contacts to be able to add a borrower.
                invisible.setEnabled(false);
                invisible.setVisibility(View.VISIBLE);
                invisible.requestFocus();
                invisible.setError("No contacts available! Must add borrower to contacts.");
                status.setChecked(true); // Set switch to available
            } else {
                // Means was previously available
                borrower_spinner.setVisibility(View.VISIBLE);
                borrower_tv.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Only need to update the view from the onCreate method
     */
    public void update() {
        if(on_create_update) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, contact_list_controller.getAllUsernames());
            borrower_spinner.setAdapter(adapter);

            item = item_list_controller.getItem(pos);
            item_controller = new ItemController(item);

            Contact contact = item_controller.getBorrower();
            if(contact != null) {
                int contact_pos = contact_list_controller.getIndex(contact);
                borrower_spinner.setSelection(contact_pos);
            }

            // Update the view
            item_controller.updateItem(itemView);
            item_controller.setItemVisibility(status, borrower_tv, borrower_spinner);
        }
    }
}