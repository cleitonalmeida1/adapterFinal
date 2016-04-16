package cleiton.adapter.model;

import java.io.Serializable;

/**
 * Created by Cleiton Gonçalves on 16/04/2016.
 */
public class Agenda implements Serializable{

    //  @PrimaryKey(autoincrement = true)
    private String id;
    //@Column
    private String nome;
    //@Column
    private String valor;
    //@Column
    private String imagem;



    public Agenda() {
    }

    public Agenda(String nome, String valor, String imagem) {
        this.nome = nome;
        this.valor = valor;
        this.imagem = imagem;
    }

    public Agenda(String id, String nome, String valor, String imagem) {
        this.nome = nome;
        this.valor = valor;
        this.imagem = imagem;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
