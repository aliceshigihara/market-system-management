import java.io.*;
import java.util.ArrayList;

public class GerenciarVendas {

    private ArrayList<Vendas> historico;
    private final String ARQUIVO_DADOS = "vendas.dat";

    public GerenciarVendas() {
        this.historico = new ArrayList<>();
        carregarDados();
    }

    public void adicionarVendas(Vendas venda) {
        historico.add(venda);
        salvarDados();
    }

    public void listarCompras(Usuarios clientes) {
        System.out.println("Veja o seu histórico de compras: " + clientes.getNome());
        boolean achou = false;

        for (Vendas v : historico) {
            if (v.getCliente() != null && v.getCliente().getLogin().equals(clientes.getLogin())) {
                System.out.println(v);
                achou = true;
            }
        }
        if (!achou) System.out.println("Nenhum compra foi realizada ainda");
    }

    public void listarVendas(Usuarios vendedor) {
        System.out.println("\n----- VENDAS REALIZADAS POR: " + vendedor.getNome() + " -----\n");
        boolean achou = false;

        for (Vendas v : historico) {
            if (v.getVendedor() != null && v.getVendedor().getLogin().equals(vendedor.getLogin())) {
                System.out.println(v);
                achou = true;
            }
        }
        if (!achou) System.out.println("Nenhuma venda foi realizada ainda");
    }

    protected void salvarDados() {
        try {
            FileOutputStream fileOut = new FileOutputStream(ARQUIVO_DADOS);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

            objOut.writeObject(historico);

            objOut.close();
            fileOut.close();
            System.out.println("Histórico salvo com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao carregar o histórico!" + e.getMessage());
        }
    }

    private void carregarDados() {
        File file = new File(ARQUIVO_DADOS);
        if (!file.exists()) {
            return;

        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            historico = (ArrayList<Vendas>) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
            historico = new ArrayList<>();
        }
    }

    public ArrayList<Vendas> getHistoricoGeral() {
        return historico;
    }
}
