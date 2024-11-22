package main.Server;

import main.ClientInterface;

public class User {
    ClientInterface clientInterface;
    public String username;
    public String password;

    public User(String username, String password, ClientInterface clientInterface) {
        this.clientInterface = clientInterface;
        this.username = username;
        this.password = password;
    }
}
