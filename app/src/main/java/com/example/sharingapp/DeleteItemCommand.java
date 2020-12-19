package com.example.sharingapp;

import java.util.concurrent.ExecutionException;

/**
 * Command to delete an item
 */
public class DeleteItemCommand extends Command {

    private Item item;

    public DeleteItemCommand(Item item) {
        this.item = item;
    }

    // Delete the item remotely from server
    public void execute() {
        if (ElasticSearchManager.removeItem(item)) {
            super.setIsExecuted(true);
        } else {
            super.setIsExecuted(false);
        }
    }
}
