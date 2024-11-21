package main;

import java.util.Date;

public class Produto {
    String name;
    float price;
    String store;
    String user_insert;
    Date date_inserted;

    public Produto(String name, float price, String store, String user_insert, Date date_inserted) {
        this.name = name;
        this.price = price;
        this.store = store;
        this.user_insert = user_insert;
        this.date_inserted = date_inserted;
    }
}
