package main;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientInterface extends Remote {

    void printonclient(String messagefromserver) throws RemoteException;
    int updated_product(List<String> changes) throws RemoteException;
}
