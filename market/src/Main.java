import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static ArrayList<Vendas> historicoVendas = new ArrayList<>();
    private static Estoque estoque = new Estoque();
    private static Usuarios usuarioLogado = null;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Usuarios.carregarDados();

        while(true) {
            if (Usuarios.usuarios.isEmpty()) {
                Usuarios.usuarios.add(new Funcionarios.Gerente("Admin","admin","1234"));

                Usuarios.salvarUsuarios();

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

        for (Usuarios u : Usuarios.usuarios) {
            if (u.autenticar(login, senha)) {
                usuarioLogado = u;
                System.out.println("\nBem-vindo(a), " + login + "!");
                return;
            }
        }
        System.out.println("Falha ao fazer login! Login ou senha invalido!");
    }

    private static void mostrarMenuPrincipal(Scanner sc) {
        if (usuarioLogado == null) {
            fazerLogin(sc);
            return;
        }

        System.out.println("\n---- MENU ----");
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
    }

    private static void processarOpcao(String opcao, Scanner sc) {
        switch (opcao) {
            case "0":
                usuarioLogado = null;
                System.out.println("Saindo da conta...");
                break;
            case "1":
                listarProduto();
                break;
            case "2":
                if (usuarioLogado instanceof Funcionarios) {
                    Vender(sc);
                } else if (usuarioLogado instanceof Clientes) {
                    System.out.println("Iniciando menu de compras...");
                    Vender(sc);
                }
                break;

            case "3":
                if (usuarioLogado instanceof Funcionarios) {
                    System.out.println("Iniciando menu de estoque...");

                } else if (usuarioLogado instanceof Clientes) {
                    System.out.println("Iniciando histórico de compras...");
                }
                break;

            case "4":
                if (usuarioLogado instanceof Funcionarios){
                    verHistoricoVendas();
                }
                break;
            case "5":
                if (usuarioLogado instanceof Funcionarios.Gerente) {
                    cadastrarProduto(sc);
                }
                break;

            case "6":
                if (usuarioLogado instanceof Funcionarios.Gerente) {
                    cadastrarFuncionario(sc);
                }
                break;

            case "7":
                if (usuarioLogado instanceof Funcionarios.Gerente) {
                    System.out.println("Iniciando relatório financeiro...");
                }
                break;

            default:
                System.out.println("Opção inválida.");
        }
    }

    private static void Vender(Scanner sc) {
        System.out.println("\nINICIANDO NOVA VENDA: ");

        ArrayList<Item> carrinho = new ArrayList<>();
        double totalVenda = 0.0;

        while (true) {
            estoque.listarEstoque();
            System.out.print("Digite o nome do produto ou (fim) para encerrar: ");
            String entrada = sc.nextLine();

            if (entrada.equalsIgnoreCase("fim")) {
                break;
            }

            Item itemEstoque = estoque.buscarProduto(entrada);

            if (itemEstoque != null) {
                System.out.println("Produto: " + itemEstoque.getProduto().getNome());
                System.out.println("Preço: " + itemEstoque.getProduto().getPreco());
                System.out.print("Quantidade desejada: ");

                int qtdCliente;
                try {
                    qtdCliente = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("❌ Quantidade inválida (digite um número).");
                    continue;
                }


                if (qtdCliente > 0 && qtdCliente <= itemEstoque.getQuantidade()) {


                    Item itemCarrinho = new Item(itemEstoque.getProduto(), qtdCliente);
                    carrinho.add(itemCarrinho);

                    totalVenda += itemEstoque.getProduto().getPreco() * qtdCliente;


                    itemEstoque.removerQuantidade(qtdCliente);

                    System.out.println("Adicionado ao carrinho!");
                } else {
                    System.out.println("Quantidade indisponível. Estoque atual: " + itemEstoque.getQuantidade());
                }

            } else {
                System.out.println("Produto não encontrado!");
            }
        }

        if (carrinho.isEmpty()) {
            System.out.println("Venda cancelada.");
            return;
        }

        Usuarios clienteCompra;
        Usuarios vendedorResponsavel;

        if (usuarioLogado instanceof Clientes) {
            clienteCompra = usuarioLogado;
            vendedorResponsavel = null;
        } else {
            vendedorResponsavel = usuarioLogado;
            System.out.print("Digite o nome do cliente: ");
            String nomeCliente = sc.nextLine();
            clienteCompra = new Clientes(nomeCliente, "temp","temp");
        }

        Vendas novaVenda = new Vendas(clienteCompra, vendedorResponsavel, carrinho, totalVenda);
        historicoVendas.add(novaVenda);

        System.out.println("✅ Venda finalizada com sucesso!");
        System.out.println(novaVenda.toString());
    }

    private static void verHistoricoVendas() {
        System.out.println("-- HISTÓRICO DE VENDAS DE: " + usuarioLogado.getNome() + " --");
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

        if (!achou) System.out.println("Você ainda não fez nenhuma venda.");
        else System.out.println("Total Acumulado: R$" + String.format("%.2f", totalVendido));
    }

    private static void cadastrarFuncionario(Scanner sc) {
        if (!(usuarioLogado instanceof Funcionarios.Gerente)) {
            System.out.println("ERRO: Apenas gerentes podem cadastrar funcionários.");
            return;
        }

        System.out.println("---- REGISTRAR NOVO FUNCIONÁRIO ---");
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        System.out.println("Selecione o tipo:");
        System.out.println("1 - Vendedor");
        System.out.println("2 - Gerente");
        String tipo = sc.nextLine();

        Usuarios novoFunc;
        if (tipo.equals("1")) {
            novoFunc = new Funcionarios.Vendedor(nome, login, senha);
        } else {
            novoFunc = new Funcionarios.Gerente(nome, login, senha);
        }

        Usuarios.usuarios.add(novoFunc);
        System.out.println("Funcionário adicionado com sucesso!");

        Usuarios.salvarUsuarios();
    }

    private static void listarProduto() {
        estoque.listarEstoque();
    }

    private static void cadastrarProduto(Scanner sc) {
        System.out.println("Cadastrando novo produto...");

        System.out.print("Nome do produto: ");
        String nome = sc.nextLine();

        System.out.print("Preço: ");
        double preco = Double.parseDouble(sc.nextLine());

        System.out.print("Quantidade inicial: ");
        int quantidade = Integer.parseInt(sc.nextLine());

        System.out.println("Selecione o Tipo:");
        Tipos[] listaTipos = Tipos.values();
        for (int i = 0; i < listaTipos.length; i++) {
            System.out.println(i + " - " + listaTipos[i]);
        }

        System.out.print("Opção: ");
        int opcaoTipo = Integer.parseInt(sc.nextLine());
        Tipos tipoSelecionado = listaTipos[opcaoTipo];

        System.out.println("Selecione a Categoria (Filtrada por " + tipoSelecionado + "): ");
        Categorias[] todasCategorias = Categorias.values();

        ArrayList<Categorias> categoriasValidas = new ArrayList<>();

        for (Categorias cat : todasCategorias) {
            if (cat.getTipo() == tipoSelecionado) {
                System.out.println(categoriasValidas.size() + " - " + cat);
                categoriasValidas.add(cat);
            }
        }

        if (categoriasValidas.isEmpty()) {
            System.out.println("Nenhuma categoria encontrada para esse tipo.");
            return;
        }

        System.out.print("Opção: ");
        int opcaoCat = Integer.parseInt(sc.nextLine());

        Categorias categoriaSelecionada = categoriasValidas.get(opcaoCat);

        Produtos novoProduto = new Produtos(nome, preco, quantidade, tipoSelecionado, categoriaSelecionada);

        estoque.cadastrarProduto(novoProduto, quantidade);
        System.out.println("Produto cadastrado com sucesso: " + nome);
    }
}