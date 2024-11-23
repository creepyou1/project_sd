package main.Client;

import main.ClientInterface;
import main.ServerInterface;
import main.Resorces.Colors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements ClientInterface {

    boolean update = false;

    protected Client() throws RemoteException {
        super();
    }

    public void updated_price(boolean update){
        this.update = update;
    }

    public static void main(String[] args) {
        Colors colors = new Colors();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            ServerInterface serverInterface = (ServerInterface) Naming.lookup("rmi://localhost:4004/server");
            Client client = new Client();

            String message;
            System.out.println(colors.change_green() + "Username : " + colors.change_back());
            String username = br.readLine();
            System.out.println(colors.change_green() + "Password : " + colors.change_back());
            String password = br.readLine();
            boolean connected = serverInterface.login(username,password,client);

            if(!connected) {
                System.err.println("Username or password incorrect.");
                System.exit(1);
            }
            else
                System.out.println("Logged in successfully.");


            while(true){
                System.out.println("=======================================");
                System.out.println("1-Adicionar Produtos");
                System.out.println("2-Remover Produtos");
                System.out.println("3-Atualizar Produtos");
                System.out.println("4-Ver Produtos");
                System.out.println("5-Sair programa");
                System.out.println("=======================================");
                String option = br.readLine();

                switch(option){
                    case "1" :
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

    }

}
