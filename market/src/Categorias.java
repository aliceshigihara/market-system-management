public enum Categorias {
    FRUTAS(Tipos.PERECIVEIS),
    BEBIDAS(Tipos.NAO_PERECIVEIS),
    DOCES(Tipos.NAO_PERECIVEIS),
    LATICINIOS(Tipos.PERECIVEIS);

    private final Tipos tipo;

    Categorias(Tipos tipo) {
        this.tipo = tipo;
    }

    public Tipos getTipo() {
        return tipo;
    }
}
