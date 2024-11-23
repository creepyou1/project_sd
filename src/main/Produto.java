package main;

import java.time.LocalDate;
import java.util.Date;

public class Produto {
    int id;
    String name;
    float price;
    String store;
    String user_insert;
    LocalDate date_inserted;

    public Produto(int id,String name, float price, String store, String user_insert, LocalDate date_inserted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.store = store;
        this.user_insert = user_insert;
        this.date_inserted = date_inserted;
    }
}
