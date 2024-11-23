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

    public void printonclient(String messagefromserver){
        System.out.println(messagefromserver);
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
                System.out.println("4-Listar Produtos");
                System.out.println("5-Sair programa");
                System.out.println("=======================================");
                String option = br.readLine();

                switch(option){
                    case "1" : {
                        System.out.println("=======================================");
                        System.out.println(colors.change_green() + "Nome do produto : " + colors.change_back());
                        String nome = br.readLine();
                        System.out.println(colors.change_green() + "Preço do produto : " + colors.change_back());
                        Float price = Float.parseFloat(br.readLine());
                        System.out.println(colors.change_green() + "Loja do produto : " + colors.change_back());
                        String store = br.readLine();
                        System.out.println("=======================================");
                        int resposta = serverInterface.register_product(nome,price,store,username);

                        if(resposta == 1){
                            System.out.println("Produto registado com sucesso.");
                        }else{
                            System.out.println("Produto já existe na loja");
                        }
                        break;
                    }
                    case "4" : serverInterface.list_products(username);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

    }

}
