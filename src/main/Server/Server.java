package main.Server;


import main.ServerInterface;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;


public class Server extends UnicastRemoteObject implements ServerInterface {

    public static List<User> users;
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

    public static void main(String[] args) {
        readusers();
        for (User user : users)
            System.out.println("Username : " + user.username + "/Password : " + user.password);

    }
}
