package main.Server;


import main.ClientInterface;
import main.ServerInterface;
import main.Produto;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;


public class Server extends UnicastRemoteObject implements ServerInterface {

    public static List<List_User> users;
    public static List<User> logged;
    public static List<Produto> produtos;
    public final static String usersFilePath = System.getProperty("user.dir") + "\\src\\main\\Resorces\\users.txt";
    public final static String productsFilePath = System.getProperty("user.dir") + "\\src\\main\\Resorces\\products.txt";
    private static Semaphore semaphore = new Semaphore(1);

    protected Server() throws RemoteException {
        super();
    }

    private static void readusers(){
        users = new ArrayList<List_User>();
        logged = new ArrayList<User>();
        produtos = new ArrayList<Produto>();

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(usersFilePath), StandardCharsets.ISO_8859_1));
                BufferedReader produ = new BufferedReader(new InputStreamReader(new FileInputStream(productsFilePath), StandardCharsets.ISO_8859_1))
                ){
            String line;
            while ((line = br.readLine()) != null) {
                String[] user = line.split(";");
                users.add(new List_User(user[0],user[1]));
            }
            while ( (line = produ.readLine()) != null ){
                String[] product = line.split(";");
                produtos.add(new Produto(Integer.parseInt(product[0]),product[1],Float.parseFloat(product[2]),product[3],product[4], LocalDate.parse(product[5])) );
            }
        } catch (FileNotFoundException e) {
            System.err.println("Main : Ficheiro não encontrado, use user1/user1 para teste do programa");
            users.add(new List_User("user1", "user1"));
        } catch (IOException e) {
            System.err.println("Main : Erro algures :(");
        }

    }
    @Override
    public boolean login(String username, String password, ClientInterface clientInterface) throws RemoteException {
        for (List_User possible : users) {
            if (possible.username.equals(username) && possible.password.equals(password)) {          // Se o username e password do cliente estiverem corretas
                logged.add(new User(username,password,clientInterface));                             // Adiciona-se este a lista dos users logados
                System.out.println("Main : User " + username + " logged in");                        // Retorna-se true para o cliente continuar o seu processo
                return true;
            }
        }
        System.out.println("Main : User failed the log in terminating connection");
        return false;
    }

    public int register_product(String name,float price,String store,String user){
        Produto p = new Produto()

        return -1;
    }

    public static void main(String[] args) {
        readusers();                                                            // O SERVIDOR COMEÇA POR LER O FICHEIRO COM UTILIZADORES E PREENCHE A LISTA USERS COM ESTES
        try {
            Server server = new Server();                                       //
            LocateRegistry.createRegistry(4004);                           //Criação do servidor e registo deste no porto 4004
            Naming.rebind("rmi://localhost:4004/server", server);         //

            System.out.println("Main : Server pronto a receber clientes");

            while(true){

            }

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }
}
