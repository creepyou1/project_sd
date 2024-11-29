package main;

import java.io.Serializable;
import java.time.LocalDate;

public class Produto implements Serializable {
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

    public LocalDate getDate_inserted() {
        return date_inserted;
    }

    public String getUser_insert() {
        return user_insert;
    }

    public String getStore() {
        return store;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setUser_insert(String user_insert) {
        this.user_insert = user_insert;
    }

    public void setDate_inserted(LocalDate date_inserted) {
        this.date_inserted = date_inserted;
    }

    public void update(String name, Float price, String store, String user_insert, LocalDate date_inserted) {
        this.name = name;
        this.price = price;
        this.store = store;
        this.user_insert = user_insert;
        this.date_inserted = date_inserted;
    }
    public String toString() {
        return "Produto :" + name + "\n" + "Preço :" + price  + "€" + "\n" + "Store :" + store + "\n" + "User Insert :" + user_insert + "\n" + "Date Insert :" + date_inserted + "\n";
    }
}
