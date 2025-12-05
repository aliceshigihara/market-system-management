import java.io.Serializable;

public class Item implements Serializable {
    private Produtos produto;
    private int quantidade;

    public Item(Produtos produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public void adicionarQuantidade(int quantidade) {
        this.quantidade += quantidade;
    }

    public void removerQuantidade(int quantidade) {
        this.quantidade -= quantidade;
    }

    public Produtos getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    @Override
    public String toString() {
        return produto.toString() + " | Quantidade em Estoque: " + quantidade;
    }
}

