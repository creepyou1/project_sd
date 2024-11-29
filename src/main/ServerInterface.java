package main;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface extends Remote {
    boolean login(String username, String password,ClientInterface clientInterface) throws RemoteException;
    int register_product(String name,float price,String store,String user) throws  RemoteException;
    int remove_product(String name,String user,String store) throws  RemoteException;
    Produto send_product(String name,String store);
    int update_product(Produto produto,String user,String[] select_product, List<Integer> changes) throws  RemoteException;
    void list_products_all(String username) throws RemoteException;
    int list_products_by_name(String username,String name) throws RemoteException;
    int list_products_by_store(String username,String store) throws RemoteException;
    void userexit(String username) throws RemoteException;
}
