package main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    boolean login(String username, String password,ClientInterface clientInterface) throws RemoteException;
    int register_product(String name,float price,String store,String user) throws  RemoteException;
    void list_products(String username) throws RemoteException;
}
