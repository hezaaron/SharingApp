package com.example.sharingapp;

import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * For remote machine: SERVER = "http://localhost:9200"
 * -------------------------------------------------------------------------------------------------
 * curl -XDELETE 'http://localhost:9200/INDEX' - can be used to delete ALL objects on the server
 * (items, users, and bids) at that index
 * view an item at: http://http://localhost:9200/INDEX/items/item_id
 * view a user at: http://http://localhost:9200/INDEX/users/user_id
 * view a bid at: http://http://localhost:9200/INDEX/bids/bid_id
 */

public class ElasticSearchManager {
    private static final String SERVER = "http://127.0.0.1:9200";
    private static final String INDEX = "sharingapp";
    private static final String ITEM_TYPE = "items";
    private static final String USER_TYPE = "users";
    private static final String BID_TYPE = "bids";
    private static JestDroidClient client;

    private ElasticSearchManager() {}
    /**
     * Returns all remote items from server
     */
    public static ArrayList<Item> getItemList () {
        verifyConfig();

        ArrayList<Item> items = new ArrayList<>();
        String search_string = "{\"from\":0,\"size\":10000}";
        Search search = new Search.Builder(search_string).addIndex(INDEX).addType(ITEM_TYPE).build();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    List<SearchResult.Hit<Item, Void>> hits = execute.getHits(Item.class);
                    items.addAll(hits.stream().map(h -> h.source).collect(Collectors.toList()));
                } else {
                    Log.i("ELASTICSEARCH", "No items found");
                }
            } catch (IOException e) {
                Log.i("ELASTICSEARCH", "Item search failed");
                e.printStackTrace();
            }
        });

        return items;
    }

    /**
     * Add item to remote server
     */
    public static Boolean addItem(Item item) {
        verifyConfig();

        List<Boolean> success = new ArrayList<>(1);
        String id = item.getId(); // Explicitly set the id to match the locally generated id
        Index index = new Index.Builder(item).index(INDEX).type(ITEM_TYPE).id(id).build();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                DocumentResult execute = client.execute(index);
                if (execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "Add item was successful");
                    Log.i("ADDED ITEM", id);
                    success.add(true);
                } else {
                    success.add(false);
                    Log.e("ELASTICSEARCH", "Add item failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return success.get(0);
    }

    /**
     * Delete item from remote server using item_id
     */
    public static Boolean removeItem(Item item) {
        verifyConfig();

        List<Boolean> success = new ArrayList<>(1);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                DocumentResult execute = client.execute(new Delete.Builder(item.getId()).index(INDEX).type(ITEM_TYPE).build());
                if (execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "Delete item was successful");
                    success.add(true);
                } else {
                    success.add(false);
                    Log.i("ELASTICSEARCH", "Delete item failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return success.get(0);
    }

    /**
     * Returns all remote users from server
     */
    public static ArrayList<User> getUserList() {
        verifyConfig();

        ArrayList<User> users = new ArrayList<>();
        String search_string = "{\"from\":0,\"size\":10000}";
        Search search = new Search.Builder(search_string).addIndex(INDEX).addType(USER_TYPE).build();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    List<SearchResult.Hit<User, Void>> hits = execute.getHits(User.class);
                    users.addAll(hits.stream().map(h -> h.source).collect(Collectors.toList()));
                } else {
                    Log.i("ELASTICSEARCH", "No users found");
                }
            } catch (IOException e) {
                Log.i("ELASTICSEARCH", "User search failed");
                e.printStackTrace();
            }
        });

        return users;
    }

    /**
     * Add user to remote server
     */
    public static Boolean addUser(User user) {
        verifyConfig();

        List<Boolean> success = new ArrayList<>(1);
        String id = user.getId(); // Explicitly set the id to match the locally generated id
        Index index = new Index.Builder(user).index(INDEX).type(USER_TYPE).id(id).build();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                DocumentResult execute = client.execute(index);
                if (execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "Add user was successful");
                    Log.i("ADDED USER", id);
                    success.add(true);
                } else {
                    success.add(false);
                    Log.e("ELASTICSEARCH", "Add User failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return success.get(0);
    }


    /**
     * Delete user from remote server using user_id
     */
    public static Boolean removeUser(User user) {
        verifyConfig();

        List<Boolean> success = new ArrayList<>(1);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                DocumentResult execute = client.execute(new Delete.Builder(user.getId()).index(INDEX).type(USER_TYPE).build());
                if (execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "Delete user was successful");
                    success.add(true);
                } else {
                    success.add(false);
                    Log.i("ELASTICSEARCH", "Delete user failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return success.get(0);
    }

    /**
     * Returns all remote bids from server
     */
    public static ArrayList<Bid> getBidList() {
        verifyConfig();

        ArrayList<Bid> bids = new ArrayList<>();
        String search_string = "{\"from\":0,\"size\":10000}";
        Search search = new Search.Builder(search_string).addIndex(INDEX).addType(BID_TYPE).build();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    List<SearchResult.Hit<Bid, Void>> hits = execute.getHits(Bid.class);
                    bids.addAll(hits.stream().map(h -> h.source).collect(Collectors.toList()));
                } else {
                    Log.i("ELASTICSEARCH", "No bids found");
                }
            } catch (IOException e) {
                Log.i("ELASTICSEARCH", "Bid search failed");
                e.printStackTrace();
            }
        });

        return bids;
    }

    /**
     * Add bid to remote server
     */
    public static Boolean addBid(Bid bid) {
        verifyConfig();

        List<Boolean> success = new ArrayList<>(1);
        String id = bid.getBidId(); // Explicitly set the id to match the locally generated id
        Index index = new Index.Builder(bid).index(INDEX).type(BID_TYPE).id(id).build();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                DocumentResult execute = client.execute(index);
                if (execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "Add bid was successful");
                    Log.i("ADDED BID", id);
                    success.add(true);
                } else {
                    success.add(false);
                    Log.e("ELASTICSEARCH", "Add bid failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return success.get(0);
    }

    /**
     * Delete bid from remote server using bid_id
     */
    public static Boolean removeBid(Bid bid) {
        verifyConfig();

        List<Boolean> success = new ArrayList<>(1);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                DocumentResult execute = client.execute(new Delete.Builder(bid.getBidId()).index(INDEX).type(BID_TYPE).build());
                if (execute.isSucceeded()) {
                    Log.i("ELASTICSEARCH", "Delete bid was successful");
                    success.add(true);
                } else {
                    success.add(false);
                    Log.i("ELASTICSEARCH", "Delete bid failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return success.get(0);
    }

    // If no client, add one
    private static void verifyConfig() {
        if(client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder(SERVER);
            DroidClientConfig config = builder.build();
            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}