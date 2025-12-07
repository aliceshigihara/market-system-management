import java.io.Serializable;

public class Produtos implements Serializable {

    private String nome;
    private double preco;
    private int quantidade;
    private Tipos tipo;
    private Categorias categoria;

    public Produtos(String nome, double preco, int quantidade, Tipos tipo, Categorias categoria) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() { return preco; }

    public int getQuantidade() { return quantidade; }

    @Override
    public String toString() {
        return "Produto: " + nome + "| Preço: R$" + preco + "| Categoria: " + categoria;
    }
}
