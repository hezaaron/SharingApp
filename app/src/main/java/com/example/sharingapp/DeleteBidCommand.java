package com.example.sharingapp;

import android.content.Context;

/**
 * Command to delete a bid
 */
public class DeleteBidCommand extends Command {

    private Bid bid;

    public DeleteBidCommand(Bid bid) {
        this.bid = bid;
    }

    // Delete the bid remotely from server
    public void execute() {
        if (ElasticSearchManager.removeBid(bid)) {
            super.setIsExecuted(true);
        } else {
            super.setIsExecuted(false);
        }
    }
}