package main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    void printonclient(String messagefromserver) throws RemoteException;
    void updated_price(boolean update) throws RemoteException;
}
