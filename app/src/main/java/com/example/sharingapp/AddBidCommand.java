package com.example.sharingapp;

import android.content.Context;

/**
 * Command to add a bid
 */
public class AddBidCommand extends Command {

    private Bid bid;

    public AddBidCommand(Bid bid) {
        this.bid = bid;
    }

    // Save the bid remotely to server
    public void execute() {
        if (ElasticSearchManager.addBid(bid)) {
            super.setIsExecuted(true);
        } else {
            super.setIsExecuted(false);
        }
    }
}
