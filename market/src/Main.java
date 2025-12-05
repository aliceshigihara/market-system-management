import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static Estoque estoque = new Estoque();
    private static ArrayList<Usuarios> usuarios = new ArrayList<>();
    private static Usuarios usuarioLogado = null;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        usuarios.add(new Funcionarios.Gerente("Alice", "admin", "admin123"));
        usuarios.add(new Clientes("Cliente1", "cliente1", "cliente123"));

        while(true) {
            if (usuarioLogado == null) {
                fazerLogin(sc);
            } else {
                mostrarMenuPrincipal(sc);
            }
        }
    }

    private static void fazerLogin(Scanner sc) {
        System.out.println("-- Bem-vindo ao Mercadinho, faça login para trabalhar ou comprar: --");
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        for (Usuarios u : usuarios) {
            if (u.autenticar(login, senha)) {
                usuarioLogado = u;
                System.out.println("Bem-vindo(a), " + u.getNome() + "!");
                return;
            }
        }
        System.out.println("Falha ao fazer login! Login ou senha invalido!");
    }

    private static void mostrarMenuPrincipal(Scanner sc) {
        System.out.println("\n-- MENU --");
        System.out.println("Pressione alguma das teclas para interagir:");
        System.out.println("1 - Listar Produtos");

        if (usuarioLogado instanceof Funcionarios) {
            System.out.println("2 - Realizar Venda");
            System.out.println("3 - Adicionar Estoque");
            System.out.println("4 - Listar suas Vendas");
        }

        if (usuarioLogado instanceof Funcionarios.Gerente) {
            System.out.println("5 - Cadastrar Novo Produto ao Estoque");
            System.out.println("6 - Cadastrar Novo Funcionário");
            System.out.println("7 - Relatório Financeiro");
        }

        if (usuarioLogado instanceof Clientes) {
            System.out.println("2 - Comprar Produto");
            System.out.println("3 - Ver minhas compras");
        }

        System.out.println("0 - Sair (LogOut)");
        System.out.print("Digite sua opção: ");
        String opcao = sc.nextLine();

        processarOpcao(opcao, sc);
        sc.nextLine();
    }

    private static void processarOpcao(String opcao, Scanner sc) {
        switch (opcao) {
            case "0":
                usuarioLogado = null;
                System.out.println("Saindo...");
                break;
            case "1":
                listarProduto();
                break;
            case "2":
                if (usuarioLogado instanceof Funcionarios) {
                    System.out.println("Iniciando menu de vendas...");
                    Vender();
                    break;
                }

                if (usuarioLogado instanceof Clientes) {
                    System.out.println("Iniciando menu de compras...");
                    break;
                }
            case "3":
                if (usuarioLogado instanceof Funcionarios) {
                    System.out.println("Iniciando menu de estoque...");
                    break;
                }

                if (usuarioLogado instanceof Clientes) {
                    System.out.println("Iniciando histórico de compras...");
                    break;
                }

            case "5":
                if (usuarioLogado instanceof Funcionarios.Gerente) {
                    System.out.println("Iniciando menu de cadastro de produtos...");
                    cadastrarProduto(sc);
                    break;
                }

            case "6":
                System.out.println("Iniciando menu de cadastro de funcionários...");
                break;

            case "7":
                System.out.println("Iniciando relatório financeiro do Mercadinho...");
                break;

        }
    }

    private static void Vender() {

    }

    private static void listarProduto() {
        estoque.listarEstoque();
        }

    private static void cadastrarProduto(Scanner sc) {
        System.out.println("Cadastrando novo produto...");

        System.out.println("Digite o nome do produto: ");
        String nome = sc.nextLine();

        System.out.println("Digite o preço do produto: ");
        double preco = sc.nextDouble();

        System.out.println("Digite o quantidade do produto: ");
        int quantidade = sc.nextInt();

        System.out.println("Selecione o tipo dele: ");
        Tipos[] listartipos = Tipos.values();
        for (int i = 0; i < listartipos.length; i++) {
            System.out.println(i + " - " + listartipos[i]);
        }

        System.out.println("Opções: ");
        int opcaoTipo = sc.nextInt();
        sc.nextLine();
        Tipos tipoSelecionado = listartipos[opcaoTipo];

        System.out.println("Selecione a categoria filtrada por " + tipoSelecionado + ": ");
        Categorias[] listarcategorias = Categorias.values();

        ArrayList<Categorias> categoriasValidas = new ArrayList<>();

        for (Categorias cat :  listarcategorias) {
            if (cat.getTipo() == tipoSelecionado) {
                System.out.println(categoriasValidas.size() + "-" + cat);
                categoriasValidas.add(cat);
            }
        }

        if (categoriasValidas.isEmpty()) {
            System.out.println("Nenhuma categoria foi selecionada :(");
            return;
        }

        System.out.println("Selecionar opção: ");
        int opcaoCat = Integer.parseInt((sc.nextLine()));

        Categorias categoriasSelecionada = Categorias.values()[opcaoCat];

        Produtos novoProduto = new Produtos(nome, preco, quantidade, tipoSelecionado, categoriasSelecionada);

        estoque.cadastrarProduto(novoProduto, quantidade);
        System.out.println("Produto cadastrado com sucesso: " + nome);
    }
}