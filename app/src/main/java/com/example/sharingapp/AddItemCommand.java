package com.example.sharingapp;

import java.util.concurrent.ExecutionException;

/**
 * Command to add an item
 */
public class AddItemCommand extends Command{

    private Item item;

    public AddItemCommand(Item item) {
        this.item = item;
    }

    // Save the item remotely to server
    public void execute(){
        if (ElasticSearchManager.addItem(item)) {
            super.setIsExecuted(true);
        } else {
            super.setIsExecuted(false);
        }
    }
}
