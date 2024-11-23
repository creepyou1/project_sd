package main;

import java.rmi.Remote;

public interface ClientInterface extends Remote {

    void updated_price(boolean update);
}
