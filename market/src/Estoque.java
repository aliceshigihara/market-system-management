import java.util.ArrayList;
import java.io.*;

public class Estoque {

    private static ArrayList<Item> itens;
    private static final String ARQUIVO_DADOS = "estoque.dat";
    private int quantidade;

    public Estoque() {
        this.itens = new ArrayList<>();

        carregarDados();
    }

    private void carregarDados() {
        try {
            File arquivo = new File(ARQUIVO_DADOS);
            if (!arquivo.exists()) {
                return;
            }

            FileInputStream fileIn = new FileInputStream(ARQUIVO_DADOS);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            this.itens = (ArrayList<Item>) objectIn.readObject();

            objectIn.close();
            fileIn.close();
            System.out.println("Dados carregados com sucesso!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar dados!" + e.getMessage());
        }
    }

    public static void salvarDados() {
        try {
            FileOutputStream fileOut = new FileOutputStream(ARQUIVO_DADOS);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(itens);

            objectOut.close();
            fileOut.close();
            System.out.println("Dados salvos com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados!" + e.getMessage());
        }
    }

    public void listarEstoque() {
        for (Item item : itens) {
            System.out.println(item);
        }
    }

    public void cadastrarProduto(Produtos p, int quantidade) {

        for (Item item : itens) {
            if (item.getProduto().getNome().equalsIgnoreCase(p.getNome())) {
                item.adicionarQuantidade(quantidade);
                System.out.println("Produto já existia, Estoque atualizado.");
                salvarDados();
                return;
            }
        }

        Item novoItem = new Item(p, quantidade);
        itens.add(novoItem);

        salvarDados();

        System.out.println("Produto cadastrado no estoque com sucesso!");

    }

    public Item buscarProduto(String buscaNome) {
        for (Item item : itens) {
            if (item.getProduto().getNome().equalsIgnoreCase(buscaNome)) {
                return item;
            }
        }
        return null;
    }

}


