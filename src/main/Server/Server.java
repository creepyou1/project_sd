package main.Server;


import main.ClientInterface;
import main.ServerInterface;
import main.Produto;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


public class Server extends UnicastRemoteObject implements ServerInterface {

    public static List<List_User> users;
    public static List<User> logged;
    public static List<Produto> produtos;
    public final static String usersFilePath = System.getProperty("user.dir") + "\\src\\main\\Server\\Resorces\\users.txt";
    public final static String productsFilePath = System.getProperty("user.dir") + "\\src\\main\\Server\\Resorces\\products.txt";
    private static Semaphore semaphore = new Semaphore(1);
    private static int prod_ids = 1;

    protected Server() throws RemoteException {
        super();
    }

    private static void readusers(){
        users = new ArrayList<List_User>();                         //Esta lista contem todos os users do ficheiro
        logged = new ArrayList<User>();                             //Esta lista contem todos os user ligados ao servidor
        produtos = new ArrayList<Produto>();                        //Esta lista contem todos os produtos

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
                prod_ids = Integer.parseInt(product[0]) + 1;
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
        for (List_User possible : users) {                                                           // Ve-se na lista dos users existentes se o que esta a tentar entrar existe
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
        boolean stop = false;

        try {
            semaphore.acquire();
            for ( Produto prod : produtos){
                if (prod.getName().equalsIgnoreCase(name) && prod.getStore().equalsIgnoreCase(store)) {
                    stop = true;
                    break;
                }
            }

            List<String> lista = new ArrayList<>();
            if (!stop){
                Produto p = new Produto(prod_ids,name,price,store,user,LocalDate.now());
                produtos.add(p);
                prod_ids++;

                for (Produto p1 : produtos)
                    lista.add(String.valueOf(p1.getId()) + ";" + p1.getName() + ";" + String.valueOf(p1.getPrice()) + ";" + p1.getStore() + ";"
                            + p1.getUser_insert() + ";" + String.valueOf(p1.getDate_inserted()));
                Files.write(Path.of(productsFilePath),lista,StandardCharsets.UTF_8);
                semaphore.release();
                return 1;
            }
        } catch (InterruptedException e) {
            System.err.println("Main : Erro ao obter o semafero");
        } catch (IOException e) {
            System.err.println("Main : Erro ao escrever no ficheiro");
        }
        semaphore.release();
        return 0;
    }

    public int remove_product(String name,String user){
        int index = 0;
        boolean success = false;
        try {
            semaphore.acquire();
            for (Produto prod : produtos) {
                if (prod.getName().equalsIgnoreCase(name)) {
                    produtos.remove(index);
                    success = true;
                    break;
                }
                index++;
            }

            List<String> lista = new ArrayList<>();
            for (Produto prod : produtos) {
                lista.add(String.valueOf(prod.getId()) + ";" + prod.getName() + ";" + String.valueOf(prod.getPrice()) + ";" + prod.getStore() + ";"
                        + prod.getUser_insert() + ";" + prod.getDate_inserted());
            }
            try {
                Files.write(Path.of(productsFilePath), lista, StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.err.println("Main : Erro ao escrever no ficheiro");
            }

            semaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (success)
            return 1;
        return 0;
    }

    public int update_product(String update_product,String name,float price,String store,String user){
        boolean success = false;

        try {
            semaphore.acquire();

            for (Produto prod : produtos){
                if (prod.getName().equalsIgnoreCase(update_product)) {
                    prod.update(name,price,store,user,LocalDate.now());
                    success = true;
                    break;
                }
            }
            List<String> lista = new ArrayList<>();
            for (Produto prod : produtos) {
                lista.add(String.valueOf(prod.getId()) + ";" + prod.getName() + ";" + String.valueOf(prod.getPrice()) + ";" + prod.getStore() + ";"
                        + prod.getUser_insert() + ";" + prod.getDate_inserted());
            }
            Files.write(Path.of(productsFilePath), lista, StandardCharsets.UTF_8);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        semaphore.release();
        if (success)
            return 1;

        return 0;
    }

    public void list_products_all(String username) {
        User u = null;

        for (User user : logged)
            if(user.username.equals(username)) {
                u = user;
                break;
            }
        for (Produto prod : produtos){
            try {
                u.clientInterface.printonclient(prod.toString());
            } catch (RemoteException e) {
                System.err.println("Main : Erro ao enviar mensagem ao cliente " + username);
            }
        }
    }

    public void list_products_by_name(String username, String name){
        User u = null;
        for (User user : logged){
            if (user.username.equals(username)) {
                u = user;
                break;
            }
        }
        for (Produto p : produtos){
            String[] name_slipt = p.getName().split(" ");

            for (int i = 0; i < name_slipt.length; i++) {
                if (name_slipt[i].equalsIgnoreCase(name)) {
                    try {
                        u.clientInterface.printonclient(p.toString());
                        break;
                    } catch (RemoteException e) {
                        System.err.println("Main : Erro ao enviar mensagem ao cliente " + username);
                    }
                }
            }
        }
    }

    public void list_products_by_store(String username, String store){
        User u = null;
        for (User user : logged){
            if (user.username.equals(username)) {
                u = user;
                break;
            }
        }
        for (Produto p : produtos)
            if (p.getStore().equalsIgnoreCase(store)) {
                try {
                    u.clientInterface.printonclient(p.toString());
                } catch (RemoteException e) {
                    System.err.println("Main : Erro ao enviar mensagem ao cliente " + username);
                }
            }
    }

    public void userexit(String username){
        System.out.println("Main : User " + username + " saiu do server");
        for (User user : logged){
            if (user.username.equals(username)) {
                logged.remove(user);
            }
        }
    }

    public static void main(String[] args) {
        readusers();                                                            // O SERVIDOR COMEÇA POR LER O FICHEIRO COM UTILIZADORES E PREENCHE A LISTA USERS COM ESTES
        try {
            Server server = new Server();                                       //
            LocateRegistry.createRegistry(4004);                           //Criação do servidor e registo deste no porto 4004
            Naming.rebind("rmi://localhost:4004/server", server);         //

            System.out.println("Main : Server pronto a receber clientes");

            while(true){}                                                       //Manter o servidor em loop para poder aceitar utilizadores

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }
}
