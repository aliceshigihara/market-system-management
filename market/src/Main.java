import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static ArrayList<Vendas> historicoVendas = new ArrayList<>();
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
        System.out.println("\n-- Bem-vindo ao Mercadinho, faça login para trabalhar ou comprar: --");
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
        System.out.println("\n---- MENU ----");
        System.out.println("Pressione alguma das teclas para interagir:");

        System.out.println("1 - Listar Produtos");

        if (usuarioLogado instanceof Funcionarios) {
            System.out.println("2 - Realizar Venda");
            System.out.println("3 - Adicionar Estoque");
            System.out.println("4 - Ver minhas Vendas");
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
                    Vender(sc);
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

            case "4":
                if (usuarioLogado instanceof Funcionarios){
                    System.out.println("Iniciando histórico de vendas...");
                    verHistoricoVendas();
                    break;
            }

            case "5":
                if (usuarioLogado instanceof Funcionarios.Gerente) {
                    System.out.println("Iniciando menu de cadastro de produtos...");
                    cadastrarProduto(sc);
                    break;
                }

            case "6":
                if (usuarioLogado instanceof Funcionarios.Gerente) {
                System.out.println("Iniciando menu de cadastro de funcionários...");
                cadastrarFuncionario(sc);
                break;
                }

            case "7":
                System.out.println("Iniciando relatório financeiro do Mercadinho...");
                break;

        }
    }

    private static void Vender(Scanner sc) {
        System.out.println("\nINICIANDO NOVA VENDA: ");

        ArrayList<Item> carrinho = new ArrayList<>();
        double totalVenda = 0.0;

        while (true) {

            estoque.listarEstoque();
            System.out.println("Digite o nome do produto ou (fim) para encerrar a venda: ");
            String entrada = sc.nextLine();

            if (entrada.equals("fim")) {
                break;
            }

            Item itemEstoque = estoque.buscarProduto(entrada);

            if (itemEstoque != null) {
                System.out.println("Produto Encontrado: " + itemEstoque.getProduto().getNome());
                System.out.println("Preço por Unidade: " + itemEstoque.getProduto().getPreco());
                System.out.println("Quantas unidades o cliente deseja?: ");

                int qtdCliente;
                try {
                    qtdCliente = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Essa quantidade não é possível");
                    continue;
                }

                if (qtdCliente > 0 && qtdCliente <= Item.getQuantidade()) {
                    Item itemCarrinho = new Item(itemEstoque.getProduto(), qtdCliente);
                    carrinho.add(itemCarrinho);

                    totalVenda += itemEstoque.getProduto().getPreco() * qtdCliente;

                    itemEstoque.removerQuantidade(qtdCliente);

                    System.out.println("Adicionado ao carrinho do cliente!");
                } else {
                    System.out.println("Não deu certo, tente novamente, veja a quantidade informada: ");
                    System.out.println(itemEstoque.getQuantidade());
                }

            } else {
                System.out.println("Algo deu errado, produto não encontrado!    ");
            }
        }

        if (carrinho.isEmpty()) {
            System.out.println("Venda Cancelada, não tem nada no carrinho");
            return;
        }

        Usuarios clientes;
        Usuarios vendedor;

        if (usuarioLogado instanceof Clientes) {

            clientes = usuarioLogado;
            vendedor = null;
        } else {
            vendedor = usuarioLogado;
            System.out.println("Digite o nome do cliente: ");
            String nomeCliente =sc.nextLine();

            clientes = new Clientes(nomeCliente, "temp","temp");
        }

        Vendas novaVenda = new Vendas(clientes, vendedor, carrinho, totalVenda);

        historicoVendas.add(novaVenda);

        System.out.println("Venda feita");
        System.out.println(novaVenda.toString());
    }

    private static void verHistoricoVendas() {
        System.out.println("-- HISTÓRICO DE VENDAS DE: " + Funcionarios.Vendedor.getNome() + " --");
        boolean achou = false;
        double totalVendido = 0;

        for (Vendas v : historicoVendas) {
            if (v.getVendedor() != null &&
            v.getVendedor().getLogin().equals(usuarioLogado.getLogin())) {

                System.out.println(v);
                totalVendido += v.getValorTotal();
                achou = true;
            }
        }

        if (!achou) System.out.println("Você ainda não fez venda nenhuma.");

        else System.out.println("Total Acumulado: R$" + String.format("%.2f", totalVendido));
    }

    private static void cadastrarFuncionario(Scanner sc) {
        if (!(usuarioLogado instanceof Funcionarios.Gerente)) {
            System.out.println("Apenas gerentes podem cadastrar novos trabalhodores...");
            return;
        }

        System.out.println("---- REGISTRAR NOVOS FUNCIONÁRIOS ---");
        System.out.println("Nome: ");
        String nome = sc.nextLine();
        System.out.println("Login: ");
        String login = sc.nextLine();
        System.out.println("Senha: ");
        String senha = sc.nextLine();

        System.out.println("\nSelecione o tipo de funcionário:");
        System.out.println("1 - Vendedor");
        System.out.println("2 - Gerente");
        String tipo = sc.nextLine();

        Usuarios novoFunc;
        if (tipo.equals("1")) {
            novoFunc = new Funcionarios.Vendedor(nome, login, senha);
        } else {
            novoFunc = new Funcionarios.Gerente(nome, login, senha);
        }

        usuarios.add(novoFunc);
        System.out.println("Funcionário adicionado com sucesso!");

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