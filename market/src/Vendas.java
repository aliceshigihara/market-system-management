import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Vendas implements Serializable {
    private Usuarios cliente;
    private Usuarios vendedor;

    private ArrayList<Item> ItensVendidos;
    private double valorTotal;
    private LocalDateTime dataHora;

    public Vendas(Usuarios cliente, Usuarios vendedor, ArrayList<Item> itensVendidos, double valorTotal) {
        this.cliente = cliente;
        this.vendedor = vendedor;
        ItensVendidos = itensVendidos;
        this.valorTotal = valorTotal;
        this.dataHora = dataHora;
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String vendedorNome = (vendedor != null) ? vendedor.getNome() : "Autoatendimento";
        return "Data: " + dataHora.format(dtf) +
                " | Cliente: " + cliente.getNome() +
                " | Vendedor: " + vendedor.getNome() +
                " | Total: R$" + String.format("%.2f", valorTotal);
    }
}
