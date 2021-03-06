package com.example.sharingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewUserActivity extends AppCompatActivity {

    private UserList user_list = new UserList();
    private UserListController user_list_controller = new UserListController(user_list);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        Intent intent = getIntent(); // Get intent from EditItemActivity
        String username_str = intent.getStringExtra("borrower_username_str");

        TextView username = (TextView) findViewById(R.id.username_right_tv);
        TextView email = (TextView) findViewById(R.id.email_right_tv);

        user_list_controller.getRemoteUsers();

        User user = user_list_controller.getUserByUsername(username_str);
        UserController user_controller = new UserController(user);

        username.setText(username_str);
        email.setText(user_controller.getEmail());
    }
}
