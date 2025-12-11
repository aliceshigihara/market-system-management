import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Vendas implements Serializable {
    private Usuarios cliente;
    private Usuarios vendedor;

    private ArrayList<Item> ItensVendidos;
    private double valorTotal;

    public Vendas(Usuarios cliente, Usuarios vendedor, ArrayList<Item> itensVendidos, double valorTotal) {
        this.cliente = cliente;
        this.vendedor = vendedor;
        ItensVendidos = itensVendidos;
        this.valorTotal = valorTotal;
    }

    public Usuarios getCliente() {
        return cliente;
    }

    public Usuarios getVendedor() {
        return vendedor;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    @Override
    public String toString() {
        String vendedorNome = (vendedor == null) ? "Autoatendimento" : vendedor.getNome();
        return
                " | Cliente: " + cliente.getNome() +
                " | Vendedor: " + vendedorNome +
                " | Total: R$" + String.format("%.2f", valorTotal);
    }

    @Override
    public String getDadosRelatorio() {
        // Aqui você define como a venda aparece no relatório oficial
        return "DATA: " + java.time.LocalDate.now() + // Exemplo se tiver data
                " | CLIENTE: " + cliente.getNome() +
                " | TOTAL: R$ " + String.format("%.2f", valorTotal);
    }
}
