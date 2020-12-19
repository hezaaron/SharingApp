package com.example.sharingapp;

import java.util.concurrent.ExecutionException;

/**
 * Command to add user
 */
public class AddUserCommand extends Command {

    private User user;

    public AddUserCommand (User user) {
        this.user = user;
    }

    // Save the user remotely to server
    public void execute() {
        if (ElasticSearchManager.addUser(user)) {
            super.setIsExecuted(true);
        } else {
            super.setIsExecuted(false);
        }
    }
}
