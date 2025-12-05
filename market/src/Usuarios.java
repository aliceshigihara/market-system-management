import java.util.ArrayList;
import java.io.Serializable;
import java.io.*;

public class Usuarios {
    protected static String nome;
    protected String login;
    protected String senha;

    private ArrayList<Usuarios> usuarios;
    private final String ARQUIVO_DADOS = "usuarios.dat";

    public Usuarios(String nome, String login, String senha) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.usuarios = new ArrayList<>();

        carregarDados();
    }

    private void carregarDados() {
        try {
            File arquivo = new File(ARQUIVO_DADOS);
            if (!arquivo.exists()) {
                return;
            }

            FileInputStream fileIn = new FileInputStream(ARQUIVO_DADOS);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            this.usuarios = (ArrayList<Usuarios>) objIn.readObject();

            objIn.close();
            fileIn.close();
            System.out.println("Usuarios carregados com sucesso!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar os usuarios!");
        }
    }

    private void salvarDados() {
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

    public static String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

}
