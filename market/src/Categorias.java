public enum Categorias {
    FRUTAS(Tipos.PERECIVEIS),
    BEBIDAS(Tipos.PERECIVEIS),
    LATICINIOS(Tipos.PERECIVEIS),
    CEREAIS(Tipos.NAO_PERECIVEIS);

    private final Tipos tipo;

    Categorias(Tipos tipo) {
        this.tipo = tipo;
    }

    public Tipos getTipo() {
        return tipo;
    }
}
