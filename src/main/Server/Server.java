package main.Server;


import main.ServerInterface;
import main.User;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class Server extends UnicastRemoteObject implements ServerInterface {

    public static List<User> users;
    public static List<User> logged;
    public final static String usersFilePath = System.getProperty("user.dir") + "\\src\\main\\Resorces\\users.txt";

    protected Server() throws RemoteException {
        super();
    }

    private static void readusers(){
        users = new ArrayList<User>();
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(usersFilePath), StandardCharsets.ISO_8859_1))
                ){
            String line;
            while ((line = br.readLine()) != null) {
                String[] user = line.split(";");
                users.add(new User(user[0],user[1]));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Main : Ficheiro não encontrado, use user1/user1 para teste do programa");
            users.add(new User("user1", "user1"));
        } catch (IOException e) {
            System.err.println("Main : Erro algures :(");
        }

    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                logged.add(user);
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        readusers();
        try {
            Server server = new Server();
            LocateRegistry.createRegistry(4004);
            Naming.rebind("rmi://localhost:6666/server", server);

            System.out.println("Main : Server pronto a receber clientes");



        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }
}
