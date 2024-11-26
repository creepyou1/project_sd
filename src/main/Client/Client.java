package main.Client;

import main.ClientInterface;
import main.ServerInterface;
import main.Server.Resorces.Colors;
import main.untils.InputValidation;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.InputMismatchException;
import java.util.Scanner;

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
        Boolean testes = false;

        try(Scanner sc = new Scanner(System.in)) {

            ServerInterface serverInterface = (ServerInterface) Naming.lookup("rmi://localhost:4004/server");
            Client client = new Client();

            String message;
            System.out.println(colors.change_green() + "Username : " + colors.change_back());
            String username = sc.nextLine();
            System.out.println(colors.change_green() + "Password : " + colors.change_back());
            String password = sc.nextLine();
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
                System.out.println("4-Procurar Produtos");
                System.out.println("5-Sair programa");
                System.out.println("=======================================");
                int option = InputValidation.validateIntBetween(sc,"->",1,5);

                switch(option){
                    case 1 : {
                        System.out.println("=======================================");
                        System.out.println(colors.change_green() + "Nome do produto : " + colors.change_back());
                        String nome = sc.nextLine();
                        Float price = 0.0F;
                        while(!testes) {
                            try {
                                System.out.println(colors.change_green() + "Preço do produto : " + colors.change_back());
                                price = sc.nextFloat();
                                testes = true;
                            } catch (InputMismatchException e) {
                                System.err.println("Preço invalido, Coloque um preço valido");
                            }
                        }
                        testes = false;
                        sc.nextLine(); //Limpar o buffer (porque)
                        System.out.println(colors.change_green() + "Loja do produto : " + colors.change_back());
                        String store = sc.nextLine();
                        System.out.println("=======================================");
                        int resposta = serverInterface.register_product(nome,price,store,username);

                        if(resposta == 1){
                            System.out.println("Produto registado com sucesso.");
                        }else{
                            System.out.println("Produto já existe na loja");
                        }
                        break;
                    }
                    case 2 : {
                        serverInterface.list_products_all(username);
                        System.out.println("=======================================");
                        System.out.println(colors.change_green() + "Nome do produto : " + colors.change_back());
                        String nome = sc.nextLine();
                        System.out.println("=======================================");
                        int resposta = serverInterface.remove_product(nome,username);
                        if(resposta == 1){
                            System.out.println("Produto removido com sucesso.");
                        }else
                            System.out.println("Produto não encontrado");
                        break;
                    }
                    case 3 : {
                        serverInterface.list_products_all(username);
                        System.out.println("=======================================");
                        System.out.println(colors.change_green() + "Nome do produto que pretende atualizar: " + colors.change_back());
                        String update_name = sc.nextLine();
                        System.out.println(colors.change_green() + "Nome do produto que pretende atualizar: " + colors.change_back());
                        System.out.println("=======================================");

                        break;
                    }
                    case 4 : {
                        System.out.println("=======================================");
                        System.out.println("1-Todos os produtos");
                        System.out.println("2-Procurar por nome");
                        System.out.println("3-Procurar por loja");
                        System.out.println("4-Retornar");
                        System.out.println("=======================================");
                        option = InputValidation.validateIntBetween(sc,"->",1,4);

                        if (option == 1)
                            serverInterface.list_products_all(username);
                        else if (option == 2){
                            System.out.println("Nome do produto : ");
                            String name = sc.nextLine();
                            serverInterface.list_products_by_name(username,name);
                        } else if (option == 3) {
                            System.out.println("Nome da loja : ");
                            String store = sc.nextLine();
                            serverInterface.list_products_by_store(username,store);
                        }

                        break;
                    }
                    case 5 : serverInterface.userexit(username); System.exit(0); break;
                }
            }

        } catch (IOException e) {
            System.err.println("O servidor esta em baixo aguarde um momento");

        } catch (NotBoundException e) {
            System.err.println("Url errado ou servidor mudou de url");
        }

    }

}
