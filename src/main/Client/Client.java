package main.Client;

import main.ClientInterface;
import main.Produto;
import main.ServerInterface;
import main.Server.Resorces.Colors;
import main.untils.InputValidation;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Client extends UnicastRemoteObject implements ClientInterface {

    static boolean update = false;
    Colors color = new Colors();
    static List<String> Changes = new ArrayList<>();

    protected Client() throws RemoteException {
        super();
    }

    public int updated_product(List<String> changes) {
        update = true;
        Changes.add(color.yellow_red() + "Alteração no seguinte produto" + color.change_back());
        for (String change : changes)
            Changes.add(color.yellow_red() + change + color.change_back());
        return 1;
    }

    public static void read_changes(){
        update = false;
        for (String change : Changes)
            System.out.println(change);
    }

    public void printonclient(String messagefromserver) {
        System.out.println(messagefromserver);
    }

    public static void main(String[] args) {
        Colors colors = new Colors();
        Boolean testes = false;

        try (Scanner sc = new Scanner(System.in)) {

            ServerInterface serverInterface = (ServerInterface) Naming.lookup("rmi://localhost:4004/server");
            Client client = new Client();

            String message;
            System.out.println(colors.change_green() + "Username : " + colors.change_back());
            String username = sc.nextLine();
            System.out.println(colors.change_green() + "Password : " + colors.change_back());
            String password = sc.nextLine();
            boolean connected = serverInterface.login(username, password, client);

            if (!connected) {
                System.err.println("Username or password incorrect.");
                System.exit(1);
            } else
                System.out.println("Logged in successfully.");


            while (true) {

                if (update)
                    read_changes();

                System.out.println("=======================================");
                System.out.println("1-Adicionar Produtos");
                System.out.println("2-Remover Produtos");
                System.out.println("3-Atualizar Produtos");
                System.out.println("4-Procurar Produtos");
                System.out.println("5-Sair programa");
                System.out.println("=======================================");
                int option = InputValidation.validateIntBetween(sc, "->", 1, 5);

                switch (option) {
                    case 1: {
                        System.out.println("=======================================");
                        System.out.println(colors.change_green() + "Nome do produto : " + colors.change_back());
                        String nome = sc.nextLine();
                        Float price = 0.0F;
                        while (!testes) {
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
                        int resposta = serverInterface.register_product(nome, price, store, username);

                        if (resposta == 1) {
                            System.out.println("Produto registado com sucesso.");
                        } else {
                            System.out.println("Produto já existe na loja");
                        }
                        break;
                    }
                    case 2: {
                        System.out.println("=======================================");
                        System.out.println("Metodo de procura preferido?\n1-Todos os produtos\n2-Procurar por nome\n3-Procurar por loja");
                        System.out.println("=======================================");
                        option = InputValidation.validateIntBetween(sc, "->", 1, 3);
                        if (option == 1) {
                            serverInterface.list_products_all(username);
                            System.out.println("Nome do produto : ");
                            String name = sc.nextLine();
                            System.out.println("Nome da Loja :");
                            String store = sc.nextLine();
                            int resposta = serverInterface.remove_product(name,username,store);
                            if (resposta == 1) {
                                System.out.println("Produto removido com sucesso.");
                            }else
                                System.out.println("Produto não encontrado");
                        } else if (option == 2) {
                            System.out.println("Nome do produto :");
                            String name = sc.nextLine();
                            int resposta = serverInterface.list_products_by_store(username, name);
                            if (resposta == 0){
                                System.out.println("produto não encontrado");
                                break;
                            }
                            System.out.println("Nome da loja : ");
                            String store = sc.nextLine();
                            resposta = serverInterface.remove_product(name,username,store);
                            if (resposta == 0){
                                System.out.println("Produto não encontrado");
                            }
                            else
                                System.out.println("Produto removido com sucesso");
                        }else if (option == 3) {
                            System.out.println("Nome da loja : ");
                            String store = sc.nextLine();
                            int resposta = serverInterface.list_products_by_store(username, store);
                            if (resposta == 0){
                                System.out.println("Loja não encontrado");
                                break;
                            }
                            System.out.println("Nome do produto :");
                            String name = sc.nextLine();
                            resposta = serverInterface.remove_product(name, username,store);
                            if (resposta == 0){
                                System.out.println("Produto não encontrado.");
                            }else
                                System.out.println("Produto removido com sucesso.");
                        }
                        break;
                    }
                    case 3: {
                        boolean sucess = false;
                        while(true) {
                            System.out.println("=======================================");
                            System.out.println("Metodo de procura preferido?\n1-Todos os produtos\n2-Procurar por nome\n3-Procurar por loja");
                            System.out.println("=======================================");
                            option = InputValidation.validateIntBetween(sc, "->", 1, 3);
                            if (option == 1) {
                                serverInterface.list_products_all(username);
                                System.out.println("Nome do produto : ");
                                String name = sc.nextLine();
                                System.out.println("Nome da Loja :");
                                String store = sc.nextLine();
                                Produto produto = serverInterface.send_product(name, store);
                                if (produto != null) {
                                    String[] selected_product = {name, store};
                                    List<Integer> options = new ArrayList<>();
                                    while (true) {
                                        System.out.println("O que pretende alterar?\n1-Nome\n2-Preço\n3-Loja\n4-Sair");
                                        option = InputValidation.validateIntBetween(sc, "->", 1, 4);

                                        if (option == 1) {
                                            System.out.println("Nome do produto : ");
                                            produto.setName(sc.nextLine());
                                        } else if (option == 2) {
                                            System.out.println("Preço do produto : ");
                                            produto.setPrice(sc.nextFloat());
                                        } else if (option == 3) {
                                            System.out.println("Loja do produto : ");
                                            produto.setStore(sc.nextLine());
                                        } else if (option == 4) {
                                            break;
                                        }

                                        boolean stop = false;
                                        for (Integer change : options) {
                                            if (change == option)
                                                stop = true;
                                        }

                                        if (!stop)
                                            options.add(option);
                                    }
                                    serverInterface.update_product(produto, username, selected_product, options);
                                }
                                else
                                    System.out.println("Produto não encontrado");

                            }
                            if (option == 2) {
                                System.out.println("Produto : ");
                                String name = sc.nextLine();
                                int resposta = serverInterface.list_products_by_name(username, name);
                                if (resposta == 1) {
                                    System.out.println("Nome da loja :");
                                    String store = sc.nextLine();
                                    Produto produto = serverInterface.send_product(name, store);
                                    if (produto != null) {
                                        String[] selected_product = {name, store};

                                        List<Integer> options = new ArrayList<>();
                                        while (true) {
                                            System.out.println("O que pretende alterar?\n1-Nome\n2-Preço\n3-Loja\n4-Sair");
                                            option = InputValidation.validateIntBetween(sc, "->", 1, 4);

                                            if (option == 1) {
                                                System.out.println("Nome do produto : ");
                                                produto.setName(sc.nextLine());
                                            } else if (option == 2) {
                                                System.out.println("Preço do produto : ");
                                                produto.setPrice(sc.nextFloat());
                                            } else if (option == 3) {
                                                System.out.println("Loja do produto : ");
                                                produto.setStore(sc.nextLine());
                                            } else if (option == 4) {
                                                break;
                                            }

                                            boolean stop = false;
                                            for (Integer change : options) {
                                                if (change == option)
                                                    stop = true;
                                            }

                                            if (!stop)
                                                options.add(option);
                                        }
                                        serverInterface.update_product(produto, username, selected_product, options);
                                    }
                                }
                            }
                            if (option == 3) {
                                System.out.println("Loja : ");
                                String store = sc.nextLine();
                                int resposta = serverInterface.list_products_by_store(username, store);
                                if (resposta == 1) {
                                    System.out.println("Nome do produto :");
                                    String name = sc.nextLine();
                                    Produto produto = serverInterface.send_product(name, store);
                                    if (produto != null) {
                                        String[] selected_product = {name, store};

                                        List<Integer> options = new ArrayList<>();
                                        while (true) {
                                            System.out.println("O que pretende alterar?\n1-Nome\n2-Preço\n3-Loja\n4-Sair");
                                            option = InputValidation.validateIntBetween(sc, "->", 1, 4);

                                            if (option == 1) {
                                                System.out.println("Nome do produto : ");
                                                produto.setName(sc.nextLine());
                                            } else if (option == 2) {
                                                System.out.println("Preço do produto : ");
                                                produto.setPrice(sc.nextFloat());
                                            } else if (option == 3) {
                                                System.out.println("Loja do produto : ");
                                                produto.setStore(sc.nextLine());
                                            } else if (option == 4) {
                                                break;
                                            }

                                            boolean stop = false;
                                            for (Integer change : options) {
                                                if (change == option)
                                                    stop = true;
                                            }

                                            if (!stop)
                                                options.add(option);
                                        }
                                        serverInterface.update_product(produto, username, selected_product, options);
                                    }
                                }
                            }
                            if (option == 4)
                                break;
                        }
                        break;
                    }
                    case 4: {
                        while (true) {
                            System.out.println("=======================================");
                            System.out.println("1-Todos os produtos");
                            System.out.println("2-Procurar por nome");
                            System.out.println("3-Procurar por loja");
                            System.out.println("4-Retornar");
                            System.out.println("=======================================");
                            option = InputValidation.validateIntBetween(sc, "->", 1, 4);

                            if (option == 1)
                                serverInterface.list_products_all(username);
                            else if (option == 2) {
                                System.out.println("Nome do produto : ");
                                String name = sc.nextLine();
                                serverInterface.list_products_by_name(username, name);
                            } else if (option == 3) {
                                System.out.println("Nome da loja : ");
                                String store = sc.nextLine();
                                serverInterface.list_products_by_store(username, store);
                            }else
                                break;

                        }
                        break;
                    }
                    case 5:
                        serverInterface.userexit(username);
                        System.exit(0);
                        break;
                }
            }

        } catch (IOException e) {
            System.err.println("O servidor esta em baixo aguarde um momento");

        } catch (NotBoundException e) {
            System.err.println("Url errado ou servidor mudou de url");
        }

    }
}

