public class Usuarios {
    protected String nome;
    protected String login;
    protected String senha;

    public Usuarios(String nome, String login, String senha) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public boolean autenticar(String login, String senha) {
        return this.login.equals(login) && this.senha.equals(senha);
    }

    public String getNome() {
        return nome;
    }

}
