package it.unicam.cs.mpgc.rpg125673.model.item;

public class Pozione implements Oggetto{
    private final String nome;
    private final String descrizione;
    private final int puntiCura;

    public Pozione(String nome, String descrizione, int puntiCura) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.puntiCura = puntiCura;
    }

    @Override
    public String getNome() {return this.nome;}

    @Override
    public String getDescrizione() {return this.descrizione;}

    public int getPuntiCura() {return this.puntiCura;}

    //Due pozioni sono uguali se hanno lo stesso nome
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pozione pozione = (Pozione) o;
        return nome.equals(pozione.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }
}
