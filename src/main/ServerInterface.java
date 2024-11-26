package main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    boolean login(String username, String password,ClientInterface clientInterface) throws RemoteException;
    int register_product(String name,float price,String store,String user) throws  RemoteException;
    int remove_product(String name,String user) throws  RemoteException;
    int update_product(String update_product,String name,float price,String store,String user) throws  RemoteException;
    void list_products_all(String username) throws RemoteException;
    void list_products_by_name(String username,String name) throws RemoteException;
    void list_products_by_store(String username,String store) throws RemoteException;
    void userexit(String username) throws RemoteException;
}
