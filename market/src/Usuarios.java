import java.util.ArrayList;
import java.io.Serializable;
import java.io.*;

public abstract class Usuarios implements Serializable {
    protected String nome;
    protected String login;
    protected String senha;

    public static ArrayList<Usuarios> usuarios = new ArrayList<>();
    private static final String ARQUIVO_DADOS = "usuarios.dat";

    public Usuarios(String nome, String login, String senha) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;

    }

    public static void carregarDados() {
        File file = new File(ARQUIVO_DADOS);
        if (!file.exists()) {
            return;

        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
            usuarios = (ArrayList<Usuarios>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
            usuarios = new ArrayList<>();
        }
    }

    public static void salvarDados() {
        try {
            FileOutputStream fileOut = new FileOutputStream(ARQUIVO_DADOS);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

            objOut.writeObject(usuarios);

            objOut.close();
            fileOut.close();
            System.out.println("Usuarios salvos com sucesso!");

        } catch (IOException e) {
            System.out.println("Erro ao carregar os usuarios!" + e.getMessage());
        }
    }

    public boolean autenticar(String login, String senha) {
        return this.login.equals(login) && this.senha.equals(senha);
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

}
