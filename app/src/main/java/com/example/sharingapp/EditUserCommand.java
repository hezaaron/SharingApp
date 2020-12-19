package com.example.sharingapp;

import java.util.concurrent.ExecutionException;

/**
 * Command used to edit pre-existing user
 */
public class EditUserCommand extends Command {

    private User old_user;
    private User new_user;

    public EditUserCommand (User old_user, User new_user){
        this.old_user = old_user;
        this.new_user = new_user;
    }

    // Delete the old user remotely, save the new user remotely to server
    public void execute() {
        if(ElasticSearchManager.addUser(new_user) && ElasticSearchManager.removeUser(old_user)) {
            super.setIsExecuted(true);
        } else {
            super.setIsExecuted(false);
        }
    }
}
