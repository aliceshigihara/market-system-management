public class Funcionarios extends Usuarios {

    public Funcionarios(String nome, String login, String senha) {
        super(nome, login, senha);
    }

    public static class Gerente extends Funcionarios {
        public Gerente(String nome, String login, String senha) {
            super(nome, login, senha);
        }
    }

    public static class Vendedor extends Funcionarios {
        public Vendedor(String nome, String login, String senha) {
            super(nome, login, senha);
        }
    }
}
