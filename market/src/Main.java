import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static GerenciarVendas gerenciarVendas = new GerenciarVendas();
    private static Estoque estoque = new Estoque();
    private static Usuarios usuarioLogado = null;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Usuarios.carregarDados();

        while(true) {
            if (Usuarios.usuarios.isEmpty()) {
                Usuarios.usuarios.add(new Funcionarios.Gerente("Admin","admin","1234"));

                Usuarios.salvarDados();

                menuInicial(sc);

            } else if (usuarioLogado == null) {
                menuInicial(sc);
            } else {
                mostrarMenuPrincipal(sc);
            }
        }
    }

    private static void fazerLogin(Scanner sc) {
        System.out.println("\n----- Bem-vindo ao Mercadinho, faça login para trabalhar ou comprar: -----\n");
        System.out.print("LOGIN: ");
        String login = sc.nextLine();
        System.out.print("SENHA: ");
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

        System.out.println("\n----- MENU DO MERCADINHO -----\n");
        System.out.println("1 - Listar Produtos");

        if (usuarioLogado instanceof Funcionarios) {
            System.out.println("2 - Realizar Venda");
            System.out.println("3 - Adicionar Estoque");
            System.out.println("4 - Ver minhas Vendas");
        }

        if (usuarioLogado instanceof Funcionarios.Gerente) {
            System.out.println("\n5 - Cadastrar Novo Produto ao Estoque");
            System.out.println("6 - Cadastrar Nova Pessoa ao Mercadinho");
            System.out.println("7 - Relatório Financeiro");
        }

        if (usuarioLogado instanceof Clientes) {
            System.out.println("2 - Comprar Produto");
            System.out.println("3 - Ver minhas compras");
        }

        System.out.println("0 - Sair (LogOut)\n");
        System.out.print("\nDigite sua opção: ");
        String opcao = sc.nextLine();

        processarOpcao(opcao, sc);
    }

    private static void processarOpcao(String opcao, Scanner sc) {
        switch (opcao) {
            case "0":
                usuarioLogado = null;
                System.out.println("\nSAINDO DA CONTA...\n");
                break;
            case "1":
                System.out.println("\n----- PRODUTOS DISPONÍVEIS NO MERCADINHO -----\n");
                listarProduto();
                break;
            case "2":
                if (usuarioLogado instanceof Funcionarios) {
                    System.out.println("\n----- PRODUTOS VENDIDOS -----\n");
                    Vender(sc);
                } else if (usuarioLogado instanceof Clientes) {
                    System.out.println("\nINICIANDO OPREAÇÃO DE MENU DE COMPRA...\n");
                    comprarProduto(sc);
                }
                break;

            case "3":
                if (usuarioLogado instanceof Funcionarios) {
                    System.out.println("\nCARREGANDO OPERAÇÃO DE ADICIONAR ESTOQUE...\n");
                    adicionarEstoque(sc);

                } else if (usuarioLogado instanceof Clientes) {
                    System.out.println("\nCARREGANDO HISTÓRICO (CLIENTE)...\n");
                    gerenciarVendas.listarCompras(usuarioLogado);
                }
                break;

            case "4":
                if (usuarioLogado instanceof Funcionarios){
                    System.out.println("\nCARREGANDO HISTÓRICO DE VENDAS...\n");
                    gerenciarVendas.listarVendas(usuarioLogado);
                }
                break;
            case "5":
                if (usuarioLogado instanceof Funcionarios.Gerente) {
                    System.out.println("\nCARREGANDO CADASTRO DE PRODUTO...\n");
                    cadastrarProduto(sc);
                }
                break;

            case "6":
                if (usuarioLogado instanceof Funcionarios.Gerente) {
                    System.out.println("\nCARREGANDO CADASTRO DE PESSOA...\n");
                    cadastrarPessoa(sc);
                }
                break;

            case "7":
                if (usuarioLogado instanceof Funcionarios.Gerente) {
                    System.out.println("\nCARREGANDO RELATÓRIO FINANCEIRO DO MERCADINHO...\n");
                    relatorioFinanceiro();
                }
                break;

            default:
                System.out.println("\nOpção inválida.\n");
        }
    }

    private static void relatorioFinanceiro() {
        ArrayList<Vendas> todasVendas = gerenciarVendas.getHistoricoGeral();

        if (todasVendas.isEmpty()) {
            System.out.println("\nNenhum Venda foi realizado.\n");
            return;
        }

        double receitaTotal = 0;
        int quantidadeVendas = 0;

        for (Vendas v : todasVendas) {
            System.out.println(v);
            System.out.println("----------------------");
            receitaTotal += v.getValorTotal();
            quantidadeVendas++;
        }

        System.out.println("\nRESUMO DAS VENDAS DO MERCADINHO");
        System.out.println("TOTAL DE VENDAS REALIZADAS: " + quantidadeVendas);
        System.out.println("RECEITA BRUTA TOTAL: R$" + receitaTotal + "\n");
    }

    private static void adicionarEstoque(Scanner sc) {
        System.out.println("-----REPOSIÇÃO DE ESTOQUE-----");
        estoque.listarEstoque();

        System.out.println("Digite o NOME do PRODUTO que você deseja adicionar estoque: ");
        String adicionar = sc.nextLine();

        Item itemEncontrado = estoque.buscarProduto(adicionar);

        if (itemEncontrado != null) {
            System.out.println("\nProduto: " + itemEncontrado.getProduto().getNome());
            System.out.println("Quantidade Atual: " + itemEncontrado.getQuantidade());

            System.out.print("Quantas unidades chegaram? ");
            int qtd;
            try {
                qtd = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\nNão dá pra adicionar");
                return;
            }

            if (qtd > 0) {

                itemEncontrado.adicionarQuantidade(qtd);
                estoque.salvarDados();

                System.out.println("\nProduto adicionado com sucesso! Nova quantidade: " + itemEncontrado.getQuantidade());
            } else {
                System.out.println("\nAlgo deu errado!");
            }
        } else {
            System.out.println("\nAlgo deu errado! Produto não encontrado");
        }
    }

    private static void Vender(Scanner sc) {
        System.out.println("\n-----INICIANDO NOVA VENDA-----");

        ArrayList<Item> carrinho = new ArrayList<>();
        double totalVenda = 0.0;

        while (true) {
            estoque.listarEstoque();
            System.out.print("\nDigite o NOME do produto ou (fim) para encerrar: ");
            String entrada = sc.nextLine();

            if (entrada.equalsIgnoreCase("fim")) {
                break;
            }

            Item itemEstoque = estoque.buscarProduto(entrada);

            if (itemEstoque != null) {
                System.out.println("\nProduto: " + itemEstoque.getProduto().getNome());
                System.out.println("Preço: " + itemEstoque.getProduto().getPreco());
                System.out.println("Quantidade atual: " + itemEstoque.getProduto().getQuantidade());
                System.out.print("Quantidade desejada: ");
                int qtdCliente;
                try {
                    qtdCliente = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("\nQuantidade inválida.");
                    continue;
                }


                if (qtdCliente > 0 && qtdCliente <= itemEstoque.getProduto().getQuantidade()) {

                    Item itemCarrinho = new Item(itemEstoque.getProduto(), qtdCliente);
                    carrinho.add(itemCarrinho);

                    totalVenda += itemEstoque.getProduto().getPreco() * qtdCliente;

                    itemEstoque.removerQuantidade(qtdCliente);
                    Estoque.salvarDados();

                    System.out.println("\nAdicionado ao carrinho!");
                } else {
                    System.out.println("\nQuantidade indisponível. Estoque atual: " + itemEstoque.getQuantidade());
                }

            } else {
                System.out.println("\nProduto não encontrado!");
            }
        }

        if (carrinho.isEmpty()) {
            System.out.println("\nVenda cancelada.");
            return;
        }

        Usuarios clienteCompra;
        Usuarios vendedorResponsavel;

        if (usuarioLogado instanceof Clientes) {
            clienteCompra = usuarioLogado;
            vendedorResponsavel = null;
        } else {
            vendedorResponsavel = usuarioLogado;
            System.out.print("\nDigite o NOME do cliente: ");
            String nomeCliente = sc.nextLine();
            clienteCompra = new Clientes(nomeCliente, "temp","temp");
        }

        Vendas novaVenda = new Vendas(clienteCompra, vendedorResponsavel, carrinho, totalVenda);
        gerenciarVendas.adicionarVendas(novaVenda);
        gerenciarVendas.salvarDados();

        System.out.println("\nVenda finalizada com sucesso!");
        System.out.println(novaVenda);
    }

    private static void cadastrarPessoa(Scanner sc) {
        if (!(usuarioLogado instanceof Funcionarios.Gerente)) {
            System.out.println("\nERRO: Apenas gerentes podem cadastrar funcionários.");
            return;
        }

        System.out.println("\n----- REGISTRAR NOVA PESSOA -----\n");
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        System.out.println("\nSELECIONE O TIPO: ");
        System.out.println("1 - Vendedor");
        System.out.println("2 - Gerente");
        System.out.println("3 - Cliente");
        String tipo = sc.nextLine();

        Usuarios novoFunc;
        if (tipo.equals("1")) {
            novoFunc = new Funcionarios.Vendedor(nome, login, senha);
        } else if  (tipo.equals("2")) {
            novoFunc = new Funcionarios.Gerente(nome, login, senha);
        } else {
            novoFunc = new Clientes(nome, login, senha);
        }

        Usuarios.usuarios.add(novoFunc);
        System.out.println("Pessoa adicionado com sucesso!");

        Usuarios.salvarDados();
    }

    private static void listarProduto() {
        estoque.listarEstoque();
    }

    private static void cadastrarProduto(Scanner sc) {
        System.out.println("\n----- CADASTRO DE PRODUTO -----\n");

        System.out.print("\nNome do produto: ");
        String nome = sc.nextLine();

        System.out.print("Preço: ");
        double preco = Double.parseDouble(sc.nextLine());

        System.out.print("Quantidade inicial: ");
        int quantidade = Integer.parseInt(sc.nextLine());

        System.out.println("\nSELECIONE O TIPO: ");
        Tipos[] listaTipos = Tipos.values();
        for (int i = 0; i < listaTipos.length; i++) {
            System.out.println(i + " - " + listaTipos[i]);
        }

        System.out.print("\nOPÇÃO: ");
        int opcaoTipo = Integer.parseInt(sc.nextLine());
        Tipos tipoSelecionado = listaTipos[opcaoTipo];

        System.out.println("\nSELECIONE A CATEGORIA (Filtrada por " + tipoSelecionado + "): ");
        Categorias[] todasCategorias = Categorias.values();

        ArrayList<Categorias> categoriasValidas = new ArrayList<>();

        for (Categorias cat : todasCategorias) {
            if (cat.getTipo() == tipoSelecionado) {
                System.out.println(categoriasValidas.size() + " - " + cat);
                categoriasValidas.add(cat);
            }
        }

        if (categoriasValidas.isEmpty()) {
            System.out.println("\nNenhuma categoria encontrada para esse tipo.\n");
            return;
        }

        System.out.print("\nOPÇÃO: ");
        int opcaoCat = Integer.parseInt(sc.nextLine());

        Categorias categoriaSelecionada = categoriasValidas.get(opcaoCat);

        Produtos novoProduto = new Produtos(nome, preco, quantidade, tipoSelecionado, categoriaSelecionada);

        estoque.cadastrarProduto(novoProduto, quantidade);
        System.out.println("\nProduto cadastrado com sucesso: " + nome);
    }

    public static void comprarProduto(Scanner sc) {
        if (!(usuarioLogado instanceof Clientes)) {
            System.out.println("\nOps! Apenas os clientes podem comprar");
            return;
        }

        System.out.println("\n----- COMPRAR PRODUTOS -----\n");
        ArrayList<Item> carrinho = new ArrayList<>();
        double totalCompra = 0.0;

        while (true) {
            estoque.listarEstoque();
            System.out.println("\nDigite o NOME do produto (ou 'fim' para fechar): ");
            String entrada = sc.nextLine();

            if (entrada.equalsIgnoreCase("fim"))
                break;

            Item itemEstoque = estoque.buscarProduto(entrada);

            if (itemEstoque != null) {
                System.out.println("\nPreço: R$" + itemEstoque.getProduto().getPreco());
                System.out.println("Quantidade: " + itemEstoque.getQuantidade());
                System.out.print("Quantas unidades você quer?: ");

                int qtd;
                try {
                    qtd = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("\nOps, essa quantidade aí não dá!");
                    continue;
                }

                if (qtd > 0 && qtd <= itemEstoque.getQuantidade()) {
                    carrinho.add(new Item(itemEstoque.getProduto(), qtd));
                    totalCompra += itemEstoque.getProduto().getPreco() * qtd;

                    itemEstoque.removerQuantidade(qtd);
                    Estoque.salvarDados();

                    System.out.println("\nProduto comprado com sucesso!");
                } else {
                    System.out.println("\nQuantidade Indisponível");
                }
            } else {
                System.out.println("\nProduto inexistente");
            }
        }

        if (carrinho.isEmpty()) {
            System.out.println("\nCompra cancelada");
            return;
        }

        Vendas novaCompra = new Vendas(usuarioLogado, null, carrinho, totalCompra);
        gerenciarVendas.adicionarVendas(novaCompra);
        gerenciarVendas.salvarDados();

        System.out.println("\nCompra realizada com sucesso, valor total: R$" + totalCompra);
        System.out.println(novaCompra);
    }

    private static void menuInicial(Scanner sc) {
        System.out.println("\n----- BEM-VINDO AO MERCADINHO -----\n");
        System.out.println("1 - Fazer Login");
        System.out.println("2 - Tornar-se Cliente");
        System.out.println("0 - Fechar Sistema");
        System.out.print("\nEscolha uma opção: ");

        String opcao = sc.nextLine();

        switch (opcao) {
            case "1":
                fazerLogin(sc);
                break;
            case "2":
                cadastroCliente(sc);
                break;
            case "0":
                System.out.println("Encerrando o sistema...");
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private static void cadastroCliente(Scanner sc) {
        System.out.println("\n----- CRIAR MINHA CONTA -----\n");

        System.out.print("Digite seu Nome: ");
        String nome = sc.nextLine();

        System.out.print("Crie um Login: ");
        String login = sc.nextLine();

        for (Usuarios u : Usuarios.usuarios) {
            if (u.getLogin().equalsIgnoreCase(login)) {
                System.out.println("❌ Erro: Este login já está em uso. Tente outro.");
                return;
            }
        }

        System.out.print("Crie uma Senha: ");
        String senha = sc.nextLine();

        Clientes novoCliente = new Clientes(nome, login, senha);

        Usuarios.usuarios.add(novoCliente);
        Usuarios.salvarDados();

        System.out.println("Conta criada com sucesso! Agora faça login para entrar.");
    }
}