package com.mycompany.marketsystem;
public class Item {
    String produto;
    int quantidade;
    
    public Item(String produto, int quantidade){
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public String getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }
    
}
